package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.path.DijkstraShortestGraphAlgorithm;
import wooteco.subway.domain.path.Path;
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

    public PathService(SectionRepository sectionRepository, StationRepository stationRepository,
            LineRepository lineRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public PathResponse calculateShortestPath(final PathRequest pathRequest) {
        final Path path = createPath(pathRequest);
        final List<Line> lines = toLines(path.findEdges());
        final int distance = path.calculateMinDistance();
        final int extraLineFare = path.findMaxExtraLineFare(lines);
        final Fare fare = Fare.of(distance, extraLineFare, pathRequest.getAge());
        final List<Station> shortestStations = path.findShortestStations();
        return new PathResponse(toStationResponses(shortestStations), distance, fare.getFare());
    }

    private Path createPath(final PathRequest pathRequest) {
        final Station startStation = stationRepository.findById(pathRequest.getSource());
        final Station endStation = stationRepository.findById(pathRequest.getTarget());
        final Sections sections = new Sections(sectionRepository.findAll());
        return new Path(startStation, endStation, DijkstraShortestGraphAlgorithm.generate(sections));
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
