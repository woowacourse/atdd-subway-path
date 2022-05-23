package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.ShortestPath;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.Stations;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;
import wooteco.subway.service.dto.PathResponse;
import wooteco.subway.service.dto.PathServiceRequest;
import wooteco.subway.service.dto.StationResponse;

@Service
public class PathService {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public PathService(SectionRepository sectionRepository, StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPath(PathServiceRequest pathRequest) {
        ShortestPath shortestPath = getShortestPath();

        List<StationResponse> stations = getShortestPathStations(pathRequest, shortestPath);
        Fare fare = new Fare();
        int shortestDistance = shortestPath.findShortestDistance(pathRequest.getSource(),
            pathRequest.getTarget());
        int fee = fare.calculate(shortestDistance);

        return new PathResponse(stations, shortestDistance, fee);
    }

    private ShortestPath getShortestPath() {
        Sections sections = new Sections(sectionRepository.findAll());
        return new ShortestPath(sections);
    }

    private List<StationResponse> getShortestPathStations(PathServiceRequest pathRequest, ShortestPath shortestPath) {
        List<Long> stationIds = shortestPath.findShortestPath(pathRequest.getSource(),
            pathRequest.getTarget());
        Stations stations = new Stations(stationRepository.findById(stationIds));
        return toStationServiceResponse(stations.findSortedStationsById(stationIds));
    }

    private List<StationResponse> toStationServiceResponse(List<Station> stations) {
        return stations.stream()
            .map(i -> new StationResponse(i.getId(), i.getName()))
            .collect(Collectors.toList());
    }
}
