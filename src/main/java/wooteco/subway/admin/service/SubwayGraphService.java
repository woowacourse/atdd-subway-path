package wooteco.subway.admin.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Edge;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Path;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.jgrapht.SubwayEdge;
import wooteco.subway.admin.repository.LineRepository;

@Service
public class SubwayGraphService implements GraphService {
    private final LineRepository lineRepository;

    public SubwayGraphService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Override
    public Optional<Path> findPath(Station source, Station target, PathType pathType) {
        WeightedMultigraph<Long, Edge> graph = new WeightedMultigraph<>(Edge.class);

        List<LineStation> lineStations = lineRepository.findAll()
                .stream()
                .flatMap(line -> line.getStations().stream())
                .collect(Collectors.toList());

        lineStations.stream()
                .map(LineStation::getStationId)
                .forEach(graph::addVertex);

        lineStations.stream()
                .filter(LineStation::isNotStarting)
                .forEach(lineStation -> graph
                        .addEdge(lineStation.getPreStationId(), lineStation.getStationId(),
                                new SubwayEdge(lineStation, pathType::weight)));
        
        try {
            GraphPath<Long, Edge> path = DijkstraShortestPath
                    .findPathBetween(graph, source.getId(), target.getId());

            return Optional.of(new Path(path.getVertexList(), path.getEdgeList()));
        } catch (NullPointerException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
