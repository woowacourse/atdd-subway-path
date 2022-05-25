package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
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

    public PathService(SectionRepository sectionRepository,
            StationRepository stationRepository,
            LineRepository lineRepository,
            ShortestPathCalculator shortestPathCalculator) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.shortestPathCalculator = shortestPathCalculator;
    }

    @Transactional
    public PathResponse calculateShortestPath(final PathRequest pathRequest) {
        final Station startStation = stationRepository.findById(pathRequest.getSource());
        final Station endStation = stationRepository.findById(pathRequest.getTarget());
        final Sections sections = new Sections(sectionRepository.findAll());

        final List<Line> lines = toLines(shortestPathCalculator.findEdges(startStation, endStation, sections));
        final Path path = shortestPathCalculator.findPath(startStation, endStation, sections, lines,
                pathRequest.getAge());
        return new PathResponse(toStationResponses(path.getStations()), path.getDistance(), path.getFare());
    }

    private List<Line> toLines(List<ShortestPathEdge> edges) {
        return edges.stream()
                .map(edge -> lineRepository.findById(edge.getLineId()))
                .collect(Collectors.toList());
    }

    private List<StationResponse> toStationResponses(final List<Station> stations) {
        return stations.stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }
}
