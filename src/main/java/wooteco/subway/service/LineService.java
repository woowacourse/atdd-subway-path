package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.exception.DataNotFoundException;
import wooteco.subway.exception.DuplicateNameException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineService(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    @Transactional
    public LineResponse createLine(final LineRequest lineRequest) {
        final Line line = lineRequest.toEntity();
        final Section section = lineRequest.toSectionEntity();
        validateDuplicateNameExist(line);
        validateStationsNames(section);

        final Line savedLine = lineDao.save(line);
        final Section sectionToSave = new Section(
                section.getUpStation(),
                section.getDownStation(),
                section.getDistance(),
                savedLine
        );
        sectionDao.save(sectionToSave);

        return LineResponse.from(savedLine, getStationsByLine(savedLine.getId()));
    }

    @Transactional(readOnly = true)
    public List<LineResponse> getAllLines() {
        final List<Line> lines = lineDao.findAll();

        return lines.stream()
                .map(line -> LineResponse.from(line, getStationsByLine(line.getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse getLineById(final Long id) {
        final Line line = findLineById(id);

        return LineResponse.from(line, getStationsByLine(line.getId()));
    }

    @Transactional(readOnly = true)
    public List<Station> getStationsByLine(final long lineId) {
        final List<Section> lineSections = sectionDao.findAllByLineId(lineId);
        final Sections sections = new Sections(lineSections);
        return sections.extractStations();
    }

    @Transactional
    public void update(final Long id, final LineRequest lineRequest) {
        final Line line = lineRequest.toEntity();
        final Line result = findLineById(id);
        validateUpdatedName(line, result);
        result.update(line);

        lineDao.update(id, result);
    }

    @Transactional
    public void delete(final Long id) {
        final int affectedRows = lineDao.deleteById(id);

        if (affectedRows == 0) {
            throw new DataNotFoundException("존재하지 않는 노선 id 입니다.");
        }
    }

    private void validateUpdatedName(final Line newLine, final Line result) {
        if (!result.getName().equals(newLine.getName())) {
            validateDuplicateNameExist(newLine);
        }
    }

    private void validateDuplicateNameExist(final Line line) {
        if (lineDao.existByName(line.getName())) {
            throw new DuplicateNameException("이미 존재하는 노선입니다.");
        }
    }

    private void validateStationsNames(final Section section) {
        findStationById(section.getUpStation().getId());
        findStationById(section.getDownStation().getId());
    }

    private Station findStationById(final Long stationId) {
        return stationDao.findById(stationId)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 지하철 역입니다."));
    }

    private Line findLineById(final Long id) {
        return lineDao.findById(id)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 노선 ID입니다."));
    }
}
