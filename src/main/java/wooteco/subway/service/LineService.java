package wooteco.subway.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.LineServiceRequest;
import wooteco.subway.service.dto.LineResponse;
import wooteco.subway.service.dto.StationResponse;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineService(LineDao lineDao, SectionDao sectionDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    @Transactional
    public LineResponse save(LineServiceRequest lineServiceRequest) {
        validateDuplicationName(lineServiceRequest.getName());
        Line line = new Line(lineServiceRequest.getName(), lineServiceRequest.getColor());
        Long savedId = lineDao.save(line);
        sectionDao.save(new Section(savedId, lineServiceRequest.getUpStationId(),
            lineServiceRequest.getDownStationId(), lineServiceRequest.getDistance()));

        return new LineResponse(savedId, line.getName(), line.getColor(), List.of(
            findStationByLineId(lineServiceRequest.getUpStationId()),
            findStationByLineId(lineServiceRequest.getDownStationId())
        ));
    }

    private void validateDuplicationName(String name) {
        if (lineDao.existsByName(name)) {
            throw new IllegalArgumentException("중복된 이름이 존재합니다.");
        }
    }

    public List<LineResponse> findAll() {
        Map<Long, Station> stations = findAllStations();
        return lineDao.findAll().stream()
            .map(i -> new LineResponse(i.getId(), i.getName(), i.getColor(),
                getSortedStationsByLineId(i.getId(), stations)))
            .collect(Collectors.toList());
    }

    private StationResponse findStationByLineId(Long lineId) {
        Station station = stationDao.findById(lineId);
        return new StationResponse(station.getId(), station.getName());
    }

    private Map<Long, Station> findAllStations() {
        return stationDao.findAll().stream()
            .collect(Collectors.toMap(Station::getId, i -> new Station(i.getName())));
    }

    private List<StationResponse> getSortedStationsByLineId(Long lineId,
        Map<Long, Station> stations) {
        Sections sections = new Sections(sectionDao.findByLineId(lineId));
        List<Long> stationIds = sections.sortedStationId();

        return stationIds.stream()
            .map(i -> toStationResponse(stations.get(i)))
            .collect(Collectors.toList());
    }

    private StationResponse toStationResponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public boolean deleteById(Long id) {
        return lineDao.deleteById(id);
    }

    public boolean updateById(Long id, LineServiceRequest lineServiceRequest) {
        Line line = new Line(id, lineServiceRequest.getName(), lineServiceRequest.getColor());
        return lineDao.updateById(line);
    }

    public LineResponse findById(Long id) {
        Optional<Line> maybeLine = lineDao.findById(id);
        Line line = maybeLine.orElseThrow(
            () -> new IllegalArgumentException("Id에 해당하는 노선이 존재하지 않습니다."));
        List<Station> stations = findSortedStationByLineId(line.getId());
        return new LineResponse(line.getId(), line.getName(), line.getColor(),
            toStationResponse(stations));
    }

    private List<StationResponse> toStationResponse(List<Station> stations) {
        return stations.stream()
            .map(i -> new StationResponse(i.getId(), i.getName()))
            .collect(Collectors.toList());
    }

    private List<Station> findSortedStationByLineId(Long lineId) {
        Sections sections = new Sections(sectionDao.findByLineId(lineId));
        List<Long> stationIds = sections.sortedStationId();
        return stationIds.stream()
            .map(stationDao::findById)
            .collect(Collectors.toList());
    }
}
