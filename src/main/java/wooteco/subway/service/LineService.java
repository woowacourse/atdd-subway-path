package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.section.Distance;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.request.LineRequest;
import wooteco.subway.dto.request.LineUpdateRequest;
import wooteco.subway.dto.request.SectionRequest;
import wooteco.subway.dto.response.LineResponse;
import wooteco.subway.dto.response.StationResponse;
import wooteco.subway.exception.DuplicateNameException;
import wooteco.subway.exception.NotFoundLineException;
import wooteco.subway.exception.NotFoundStationException;

@Service
@Transactional
public class LineService {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public LineService(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public LineResponse createLine(LineRequest lineRequest) {
        validateDuplicateName(lineRequest.getName());

        Line newLine = generateLineFromLineRequest(lineRequest);
        Line createdLine = lineDao.save(newLine);
        Section section = generateSectionFromLineRequest(lineRequest);

        saveSections(List.of(section), createdLine.getId());
        return LineResponse.from(createdLine, generateStationResponsesByLineId(createdLine.getId()));
    }

    private Line generateLineFromLineRequest(LineRequest lineRequest) {
        return new Line(lineRequest.getName(), lineRequest.getColor(), new Fare(lineRequest.getExtraFare()));
    }

    private Section generateSectionFromLineRequest(LineRequest lineRequest) {
        Station upStation = stationDao.findById(lineRequest.getUpStationId())
                .orElseThrow(NotFoundStationException::new);
        Station downStation = stationDao.findById(lineRequest.getDownStationId())
                .orElseThrow(NotFoundStationException::new);
        return new Section(upStation, downStation, new Distance(lineRequest.getDistance()));
    }

    private void saveSections(List<Section> sections, Long lineId) {
        for (Section section : sections) {
            sectionDao.save(lineId, section);
        }
    }

    private void validateDuplicateName(String name) {
        boolean isExisting = lineDao.findByName(name).isPresent();

        if (isExisting) {
            throw new DuplicateNameException();
        }
    }

    public List<LineResponse> getAllLines() {
        return lineDao.findAll()
                .stream()
                .map(line -> LineResponse.from(line, generateStationResponsesByLineId(line.getId())))
                .collect(Collectors.toList());
    }

    public LineResponse getLineById(Long id) {
        return lineDao.findById(id)
                .map(line -> LineResponse.from(line, generateStationResponsesByLineId(id)))
                .orElseThrow(NotFoundLineException::new);
    }

    public void update(Long id, LineUpdateRequest line) {
        validateExistingId(id);
        validateDuplicateName(line.getName());
        lineDao.update(id, line.getName(), line.getColor());
    }

    public void delete(Long id) {
        validateExistingId(id);
        lineDao.deleteById(id);
        sectionDao.deleteAllSectionsByLineId(id);
    }

    private void validateExistingId(Long id) {
        boolean isExisting = lineDao.findById(id).isPresent();

        if (!isExisting) {
            throw new NotFoundLineException();
        }
    }

    private List<StationResponse> generateStationResponsesByLineId(Long lineId) {
        Line foundLine = lineDao.findById(lineId).orElseThrow(NotFoundLineException::new);
        List<Section> sections = foundLine.getSections().getValue();

        return Stream.concat(
                sections.stream().map(Section::getDownStation),
                sections.stream().map(Section::getUpStation)
        ).distinct().map(StationResponse::from).collect(Collectors.toList());
    }

    public void createSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineDao.findById(lineId).orElseThrow(NotFoundLineException::new);

        Station upStation = getStation(sectionRequest.getUpStationId());
        Station downStation = getStation(sectionRequest.getDownStationId());
        Distance distance = new Distance(sectionRequest.getDistance());

        line.addSection(new Section(upStation, downStation, distance));
        deleteAndCreateSections(lineId, line);
    }

    private Station getStation(Long stationId) {
        return stationDao.findById(stationId)
                .orElseThrow(NotFoundStationException::new);
    }

    public void deleteStationById(Long lineId, Long stationId) {
        Line line = lineDao.findById(lineId).orElseThrow(NotFoundLineException::new);
        Station station = stationDao.findById(stationId).orElseThrow(NotFoundStationException::new);

        line.deleteStation(station);
        deleteAndCreateSections(lineId, line);
    }

    private void deleteAndCreateSections(Long lineId, Line line) {
        sectionDao.deleteAllSectionsByLineId(lineId);
        saveSections(line.getSections().getValue(), lineId);
    }
}
