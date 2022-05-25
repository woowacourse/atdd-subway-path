package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.FareStrategy;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.ShortestPathStrategy;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

@Service
public class PathService {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;
    private final ShortestPathStrategy pathStrategy;
    private final FareStrategy fareStrategy;

    public PathService(SectionRepository sectionRepository, StationRepository stationRepository,
            ShortestPathStrategy pathStrategy, FareStrategy fareStrategy) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
        this.pathStrategy = pathStrategy;
        this.fareStrategy = fareStrategy;
    }

    @Transactional
    public PathResponse calculateShortestPath(final PathRequest pathRequest) {
        Station startStation = stationRepository.findById(pathRequest.getSource());
        Station endStation = stationRepository.findById(pathRequest.getTarget());
        Sections sections = new Sections(sectionRepository.findAll());

        final Path path = pathStrategy.findPath(startStation, endStation, sections);
        final int fare = fareStrategy.calculateFare(path.getDistance());
        return new PathResponse(toStationResponses(path.getStations()), path.getDistance(), fare);
    }

    private List<StationResponse> toStationResponses(final List<Station> stations) {
        return stations.stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }
}
