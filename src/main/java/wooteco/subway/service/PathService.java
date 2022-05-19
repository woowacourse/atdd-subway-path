package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.path.DijkstraStrategy;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse searchPath(PathRequest pathRequest) {
        Path path = new Path(stationDao.findAll(), sectionDao.findAll(), new DijkstraStrategy());

        List<Long> shortestPath = path.getShortestPath(pathRequest.getSource(), pathRequest.getTarget());
        int distance = path.calculateShortestDistance(pathRequest.getSource(), pathRequest.getTarget());

        Fare fare = new Fare(distance, pathRequest.getAge());

        return new PathResponse(createStationResponseOf(shortestPath), distance, fare.calculateFare());
    }

    private List<StationResponse> createStationResponseOf(List<Long> path) {
        List<Station> stations = stationDao.findByIdIn(path);

        Map<Long, String> stationMap = stations.stream()
                .collect(Collectors.toMap(Station::getId, Station::getName));

        return path.stream()
                .map(node -> new StationResponse(node, stationMap.get(node)))
                .collect(Collectors.toList());
    }
}
