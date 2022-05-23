package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.section.ConcreteCreationStrategy;
import wooteco.subway.domain.section.ConcreteDeletionStrategy;
import wooteco.subway.domain.section.ConcreteSortStrategy;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.StationResponse;

@Service
public class LineService {
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public LineService(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Transactional
    public LineResponse save(LineRequest lineRequest) {
        Station downStation = stationDao.getById(lineRequest.getDownStationId());
        Station upStation = stationDao.getById(lineRequest.getUpStationId());

        Line line = lineRequest.toLine();
        checkDuplication(line);
        Line newLine = lineDao.insert(line);

        Section section = new Section(newLine.getId(), upStation.getId(), downStation.getId(), lineRequest.getDistance());
        sectionDao.insert(section);

        return new LineResponse(newLine.getId(), newLine.getName(), newLine.getColor(), newLine.getExtraFare(), createStationResponseOf(newLine));
    }

    private void checkDuplication(Line line) {
        if(lineDao.existByNameOrColor(line)){
            throw new IllegalArgumentException("이미 존재하는 노선 이름 혹은 색입니다.");
        }
    }

    public List<LineResponse> findAll() {
        List<Line> lines = lineDao.findAll();
        return lines.stream()
                .map(line -> new LineResponse(line.getId(), line.getName(), line.getColor(), line.getExtraFare(), createStationResponseOf(line)))
                .collect(Collectors.toList());
    }

    public LineResponse findById(Long id) {
        Line line = lineDao.getById(id);
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getExtraFare(), createStationResponseOf(line));
    }

    private List<StationResponse> createStationResponseOf(Line line) {
        Sections sections = new Sections(sectionDao.getByLineId(line.getId()), new ConcreteCreationStrategy(), new ConcreteDeletionStrategy(), new ConcreteSortStrategy());
        List<Station> stations = stationDao.findByIdIn(sections.getStationIds());

        List<Station> sortedStations = sections.sort(stations);

        return sortedStations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }

    public void edit(Long id, String name, String color, int extraFare) {
        lineDao.edit(new Line(id, name, color, extraFare));
    }

    public void delete(Long id) {
        lineDao.deleteById(id);
    }
}
