package wooteco.subway.path.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.exception.NoPathException;
import wooteco.subway.station.domain.Station;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Subway {
    private final List<Line> lines;

    public Subway(List<Line> lines) {
        this.lines = lines;
    }

    public GraphPath<Station, DefaultWeightedEdge> findPath(Station sourceStation, Station targetStation) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = graphFromLines();
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(sourceStation, targetStation);
        if (Objects.isNull(graphPath)) {
            throw new NoPathException("최단 경로가 존재하지 않습니다.");
        }
        return graphPath;
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> graphFromLines() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVerticesToGraph(graph);
        setEdgeWeights(graph);
        return graph;
    }

    private void setEdgeWeights(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Line line : lines) {
            setEdgeWeightsForLine(graph, line.getSections());
        }
    }

    private void setEdgeWeightsForLine(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Sections sections) {
        for (Section section : sections.getSections()) {
            graph.setEdgeWeight(
                    graph.addEdge(section.getUpStation(), section.getDownStation()),
                    section.getDistance()
            );
        }
    }

    private void addVerticesToGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        Set<Station> stations = new HashSet<>();
        for(Line line : lines) {
            stations.addAll(line.getStations());
        }
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }
}
