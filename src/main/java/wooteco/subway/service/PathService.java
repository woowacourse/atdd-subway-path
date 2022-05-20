package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.CostCalculator;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.ShortestPath;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.PathResponse;
import wooteco.subway.service.dto.StationResponse;

@Service
public class PathService {

    private static final String NO_STATION_ERROR = "해당 id인 역은 존재하지 않습니다";
    private static final String CALL_MYSELF_ERROR = "출발지와 도착지가 달라야 합니다.";

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse searchPaths(Long sourceId, Long targetId) {
        validRequest(sourceId, targetId);
        List<Section> sections = sectionDao.findAll();
        List<Station> stations = stationDao.findAll();

        ShortestPath shortestPath = new ShortestPath(stations, sections);
        Station source = findById(stations, sourceId);
        Station target = findById(stations, targetId);

        int distance = shortestPath.calculateDistance(source, target);

        return new PathResponse(
                distance,
                CostCalculator.calculate(distance, 0),
                generateStationResponses(shortestPath.findPath(source, target))
        );
    }

    private void validRequest(Long sourceId, Long targetId) {
        if (sourceId.equals(targetId)) {
            throw new IllegalArgumentException(CALL_MYSELF_ERROR);
        }
    }

    private Station findById(List<Station> stations, Long id) {
        return stations.stream()
                .filter(it -> id.equals(it.getId()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(NO_STATION_ERROR));
    }

    private List<StationResponse> generateStationResponses(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }
}
