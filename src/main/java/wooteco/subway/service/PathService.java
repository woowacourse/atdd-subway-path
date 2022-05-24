package wooteco.subway.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.fare.FareStrategy;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.ShortestPathCalculator;
import wooteco.subway.domain.path.ShortestPathEdge;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

@Service
public class PathService {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final ShortestPathCalculator shortestPathCalculator;
    private final FareStrategy fareStrategy;

    public PathService(SectionRepository sectionRepository,
            StationRepository stationRepository,
            LineRepository lineRepository,
            ShortestPathCalculator shortestPathCalculator,
            FareStrategy fareStrategy) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.shortestPathCalculator = shortestPathCalculator;
        this.fareStrategy = fareStrategy;
    }

    @Transactional
    public PathResponse calculateShortestPath(final PathRequest pathRequest) {
        final Station startStation = stationRepository.findById(pathRequest.getSource());
        final Station endStation = stationRepository.findById(pathRequest.getTarget());
        final Sections sections = new Sections(sectionRepository.findAll());

        final Path path = shortestPathCalculator.findPath(startStation, endStation, sections);
        final List<Line> lines = toLines(shortestPathCalculator.findEdges(startStation, endStation, sections));
        final int fare = fareStrategy.calculateFare(path.getDistance(), findMaxExtraLineFare(lines));
        return new PathResponse(toStationResponses(path.getStations()), path.getDistance(), fare);
    }

    private List<Line> toLines(List<ShortestPathEdge> edges) {
        return edges.stream()
                .map(edge -> lineRepository.findById(edge.getLineId()))
                .collect(Collectors.toList());
    }

    private int findMaxExtraLineFare(final List<Line> lines) {
        return lines.stream()
                .map(Line::getExtraFare)
                .max(Comparator.comparingInt(o -> o))
                .orElse(0);
    }

    private List<StationResponse> toStationResponses(final List<Station> stations) {
        return stations.stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }
}
