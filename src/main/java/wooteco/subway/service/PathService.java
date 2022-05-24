package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.element.Station;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.fare.PolicyFactory;
import wooteco.subway.domain.fare.policy.FarePolicy;
import wooteco.subway.domain.path.GraphFactory;
import wooteco.subway.domain.path.Path;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;
import wooteco.subway.service.dto.request.PathsRequest;
import wooteco.subway.service.dto.response.PathResponse;
import wooteco.subway.service.dto.response.StationResponse;

@Service
public class PathService {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public PathService(SectionRepository sectionRepository, StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse showPaths(PathsRequest pathsRequest) {
        Path path = Path.create(GraphFactory.getGraph(sectionRepository.findAll()),
                stationRepository.findById(pathsRequest.getSource()),
                stationRepository.findById(pathsRequest.getTarget()));
        return toPathResponse(path, pathsRequest.getAge());
    }

    private PathResponse toPathResponse(Path path, int age) {
        List<FarePolicy> policies = List.of(
                PolicyFactory.createLineFee(path.getLines()),
                PolicyFactory.createAgeDiscount(age)
        );
        int baseFare = PolicyFactory.createDistance(path.getDistance()).getFare(path.getDistance());
        return new PathResponse(
                toStationResponse(path.getStations()),
                path.getDistance(),
                new Fare(policies).getFare(baseFare));
    }

    private List<StationResponse> toStationResponse(List<Station> route) {
        return route.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }
}
