package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.strategy.DijkstraPathStrategy;
import wooteco.subway.domain.strategy.FareStrategy;
import wooteco.subway.domain.strategy.PathStrategy;
import wooteco.subway.dto.request.PathRequest;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.dto.response.StationResponse;

@Service
public class PathService {

    private final StationService stationService;
    private final SectionDao sectionDao;

    public PathService(StationService stationService, SectionDao sectionDao) {
        this.stationService = stationService;
        this.sectionDao = sectionDao;
    }

    public PathResponse findShortestPath(PathRequest pathRequest) {
        PathStrategy pathStrategy = new DijkstraPathStrategy();

        Station source = stationService.findById(pathRequest.getSource());
        Station target = stationService.findById(pathRequest.getTarget());
        Sections sections = sectionDao.findAll();

        Path path = pathStrategy.calculatePath(source, target, sections);

        return createResponse(pathRequest, path);
    }

    private PathResponse createResponse(PathRequest pathRequest, Path path) {
        FareStrategy fareStrategy = new FareStrategy();

        int distance = path.getDistance();
        int extraFare = path.getMostExpensiveExtraFare();
        int age = pathRequest.getAge();

        List<StationResponse> stations = toResponses(path.getStations());
        int fare = fareStrategy.calculateFare(distance, extraFare, age);

        return new PathResponse(stations, distance, fare);
    }

    private List<StationResponse> toResponses(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }

}
