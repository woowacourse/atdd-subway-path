package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.path.DijkstraStrategy;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse searchPath(Long source, Long target, int age) {
        Path path = new Path(stationDao.findAll(), sectionDao.findAll(), new DijkstraStrategy());

        List<Long> shortestPath = path.getShortestPath(source, target);
        int distance = path.calculateShortestDistance(source, target);

        Fare fare = new Fare(distance, age);

        return new PathResponse(createStationResponseOf(shortestPath), distance, fare.calculateFare());
    }

    private List<StationResponse> createStationResponseOf(List<Long> path) {
        List<Station> stations = arrangeStations(path);

        return stations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }

    private List<Station> arrangeStations(List<Long> stationIds) {
        List<Station> stations = stationDao.findByIdIn(stationIds);

        List<Station> sortedStations = new ArrayList<>();
        for (Long stationId : stationIds) {
            stations.stream()
                    .filter(station -> station.isSameId(stationId))
                    .findFirst()
                    .ifPresent(sortedStations::add);
        }
        return sortedStations;
    }
}
