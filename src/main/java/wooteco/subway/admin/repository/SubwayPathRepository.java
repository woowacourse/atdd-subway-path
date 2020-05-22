package wooteco.subway.admin.repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.admin.domain.Edge;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Path;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.jgrapht.SubwayEdge;

@Component
public class SubwayPathRepository implements PathRepository {
    private final LineRepository lineRepository;

    public SubwayPathRepository(LineRepository lineRepository) {
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

        GraphPath<Long, Edge> path = DijkstraShortestPath
                .findPathBetween(graph, source.getId(), target.getId());

        if (Objects.isNull(path)) {
            return Optional.empty();
        }

        return Optional.of(new Path(path.getVertexList(), path.getEdgeList()));
    }
}
