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
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.LineUpdateRequest;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationResponse;
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
        Line newLine = getLineFromLineRequest(lineRequest);
        validateDuplicateName(newLine.getName());

        Line savedLine = saveLineWithSections(newLine);
        return LineResponse.from(savedLine, getStationResponsesByLineId(savedLine.getId()));
    }

    private Line getLineFromLineRequest(LineRequest lineRequest) {
        String name = lineRequest.getName();
        String color = lineRequest.getColor();
        Fare extraFare = new Fare(lineRequest.getExtraFare());
        Section section = getSectionFromLineRequest(lineRequest);
        return new Line(name, color, extraFare, section);
    }

    private Section getSectionFromLineRequest(LineRequest lineRequest) {
        Station upStation = getStation(lineRequest.getUpStationId());
        Station downStation = getStation(lineRequest.getDownStationId());
        return new Section(upStation, downStation, new Distance(lineRequest.getDistance()));
    }

    private Line saveLineWithSections(Line line) {
        Line savedLine = lineDao.save(line);
        for (Section section : line.getSections().getValue()) {
            sectionDao.save(savedLine.getId(), section);
        }

        return savedLine;
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
                .map(line -> LineResponse.from(line, getStationResponsesByLineId(line.getId())))
                .collect(Collectors.toList());
    }

    public LineResponse getLineById(Long id) {
        return lineDao.findById(id)
                .map(line -> LineResponse.from(line, getStationResponsesByLineId(id)))
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

    private List<StationResponse> getStationResponsesByLineId(Long lineId) {
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

        line.getSections().deleteStation(station);
        deleteAndCreateSections(lineId, line);
    }

    private void deleteAndCreateSections(Long lineId, Line line) {
        sectionDao.deleteAllSectionsByLineId(lineId);
        for (Section section : line.getSections().getValue()) {
            sectionDao.save(lineId, section);
        }
    }
}
