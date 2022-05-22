package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.section.Distance;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.LineUpdateRequest;
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

    public LineResponse createLine(LineRequest line) {
        Station upStation = stationDao.findById(line.getUpStationId()).orElseThrow(NotFoundStationException::new);
        Station downStation = stationDao.findById(line.getDownStationId()).orElseThrow(NotFoundStationException::new);
        Section newSection = new Section(upStation, downStation, new Distance(line.getDistance()));
        Line newLine = new Line(line.getName(), line.getColor(), newSection);
        validateDuplicateName(newLine);

        Line createdLine = lineDao.save(newLine);
        sectionDao.save(createdLine.getId(), newSection);
        return LineResponse.from(createdLine, getStationResponsesByLineId(createdLine.getId()));
    }

    private void validateDuplicateName(Line line) {
        boolean isExisting = lineDao.findByName(line.getName()).isPresent();

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
        Line foundLine = lineDao.findById(id).orElseThrow(NotFoundLineException::new);
        Line newLine = new Line(id, line.getName(), line.getColor(), foundLine.getSections());
        validateExistById(id);
        lineDao.update(id, newLine);
    }

    public void delete(Long id) {
        validateExistById(id);
        lineDao.deleteById(id);
    }

    private void validateExistById(Long id) {
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
}
