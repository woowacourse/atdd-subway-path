package wooteco.subway.path.domain;

import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Path {

    private final List<Section> sections = new ArrayList<>();
    private final WeightedGraph<Station, DefaultWeightedEdge> graph;
    private final ShortestPathAlgorithm<Station, DefaultWeightedEdge> shortestPath;

    public Path(List<Line> lines, WeightedGraphStrategy weightedGraphStrategy, ShortestPathStrategy shortestPathStrategy) {
        for (Line line : lines) {
            Sections sections = line.getSections();
            this.sections.addAll(sections.getSections());
        }
        graph = weightedGraphStrategy.match();
        initGraph();
        shortestPath = shortestPathStrategy.match(graph);
    }

    private void initGraph() {
        List<Station> stations = setDistinctStations();
        initVertices(stations);
        initEdges();
    }

    private List<Station> setDistinctStations() {
        Set<Station> distinctStations = new HashSet<>();
        for (Section section : sections) {
            distinctStations.add(section.getUpStation());
            distinctStations.add(section.getDownStation());
        }
        return new ArrayList<>(distinctStations);
    }

    private void initVertices(List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void initEdges() {
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    public List<Station> getShortestPath(Station source, Station target) {
        return shortestPath.getPath(source, target)
                .getVertexList();
    }

    public int getTotalDistance(Station source, Station target) {
        return (int) shortestPath.getPath(source, target)
                .getWeight();
    }
}
