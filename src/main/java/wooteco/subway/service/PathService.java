package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.PathCalculator;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.ShortestPathCalculator;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

@Transactional(readOnly = true)
@Service
public class PathService {

    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;
    private final PathCalculator pathCalculator;

    public PathService(StationRepository stationRepository, SectionRepository sectionRepository,
                       PathCalculator pathCalculator) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
        this.pathCalculator = pathCalculator;
    }

    public PathResponse calculateMinDistance(final PathRequest pathRequest) {
        Sections sections = new Sections(sectionRepository.findAll());
        Station startStation = stationRepository.findById(pathRequest.getSource());
        Station endStation = stationRepository.findById(pathRequest.getTarget());

        Path path = new Path(pathCalculator, startStation, endStation);
        List<StationResponse> stationResponses = toStationResponses(path.findShortestStations(sections));
        int distance = path.calculateMinDistance(sections);
        int fare = path.calculateFare(sections);
        return new PathResponse(stationResponses, distance, fare);
    }

    private List<StationResponse> toStationResponses(final List<Station> stations) {
        return stations.stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }

}
