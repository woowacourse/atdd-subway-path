package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

@Service
public class PathService {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public PathService(SectionRepository sectionRepository, StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public PathResponse calculateMinDistance(final PathRequest pathRequest) {
        Station startStation = stationRepository.findById(pathRequest.getSource());
        Station endStation = stationRepository.findById(pathRequest.getTarget());
        Sections sections = new Sections(sectionRepository.findAll());
        Path path = Path.of(startStation, endStation, sections);

        List<StationResponse> stationResponses = toStationResponses(path.findShortestStations());
        int distance = path.calculateMinDistance();
        int fare = path.calculateFare(distance);
        return new PathResponse(stationResponses, distance, fare);
    }

    private List<StationResponse> toStationResponses(final List<Station> stations) {
        return stations.stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }
}
