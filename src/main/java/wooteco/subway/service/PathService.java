package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.path.*;
import wooteco.subway.domain.path.factory.JgraphtPathFactory;
import wooteco.subway.domain.path.factory.PathFactory;
import wooteco.subway.domain.path.strategy.DijkstraStrategy;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.repository.PathRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final PathRepository pathRepository;

    public PathService(PathRepository pathRepository) {
        this.pathRepository = pathRepository;
    }

    public PathResponse searchPath(PathRequest pathRequest) {
        Path shortestPath = getShortestPath(pathRequest);
        List<Station> shortestPathStations = shortestPath.getStations();
        int distance = shortestPath.calculateDistance();
        int fare = getFare(shortestPath, distance, pathRequest.getAge());

        return new PathResponse(createStationResponseOf(shortestPathStations), distance, fare);
    }

    private Path getShortestPath(PathRequest pathRequest) {
        Station sourceStation = pathRepository.findStationById(pathRequest.getSource());
        Station targetStation = pathRepository.findStationById(pathRequest.getTarget());

        PathFactory pathFactory = JgraphtPathFactory.of(pathRepository.findAllSections(), new DijkstraStrategy());
        return Path.of(pathFactory, sourceStation, targetStation);
    }

    private int getFare(Path path, int distance, int age) {
        Map<Long, Integer> lineExtraFares = pathRepository.getLineExtraFares();
        int extraFare = path.getPathExtraFare(lineExtraFares);
        Fare fare = Fare.of(distance, age, extraFare);

        return fare.calculateFare();
    }

    private List<StationResponse> createStationResponseOf(List<Station> path) {
        return path.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }
}
