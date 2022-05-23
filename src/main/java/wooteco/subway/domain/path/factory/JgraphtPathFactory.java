package wooteco.subway.domain.path.factory;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.strategy.PathFindingStrategy;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

import java.util.List;

public class JgraphtPathFactory implements PathFactory {
    private final Graph<Station, Section> graph;
    private final PathFindingStrategy pathFindingStrategy;

    private JgraphtPathFactory(Graph<Station, Section> graph, PathFindingStrategy pathFindingStrategy) {
        this.graph = graph;
        this.pathFindingStrategy = pathFindingStrategy;
    }

    public static JgraphtPathFactory of(List<Section> sections, PathFindingStrategy pathFindingStrategy) {
        WeightedMultigraph<Station, Section> subway = new WeightedMultigraph<>(Section.class);
        for (Section section : sections) {
            subway.addVertex(section.getUpStation());
            subway.addVertex(section.getDownStation());

            subway.addEdge(section.getUpStation(), section.getDownStation(), section);
        }
        return new JgraphtPathFactory(subway, pathFindingStrategy);
    }

    public Path createShortestPath(Station source, Station target) {
        GraphPath<Station, Section> shortestPath = pathFindingStrategy.findPathBetween(graph, source, target);
        return new Path(shortestPath.getEdgeList(), shortestPath.getVertexList());
    }
}
