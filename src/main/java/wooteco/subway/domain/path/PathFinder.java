package wooteco.subway.domain.path;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.Multigraph;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

public class PathFinder {

    private final Multigraph<Station, ShortestPathEdge> graph;

    public PathFinder(Multigraph<Station, ShortestPathEdge> graph, List<Section> sections) {
        this.graph = graph;
        addVertex(sections, graph);
        addEdge(sections, graph);
    }

    public Path getPath(Station upStation, Station downStation) {
        DijkstraShortestPath<Station, ShortestPathEdge> shortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, ShortestPathEdge> path = shortestPath.getPath(upStation, downStation);
        int extraFare = path.getEdgeList()
                .stream()
                .mapToInt(edge -> edge.getExtraFare())
                .max()
                .orElseGet(() -> 0);

        return new Path(path.getVertexList(),
                (int) path.getWeight(),
                extraFare);
    }

    private void addVertex(List<Section> sections, Multigraph<Station, ShortestPathEdge> graph) {
        Set<Station> stations = getStations(sections);
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void addEdge(List<Section> sections, Multigraph<Station, ShortestPathEdge> graph) {
        for (Section section : sections) {
            Line line = section.getLine();
            int distance = section.getDistance();
            graph.addEdge(section.getUpStation(), section.getDownStation(), new ShortestPathEdge(line.getExtraFare(), distance));
        }
    }

    private static Set<Station> getStations(List<Section> sections) {
        Set<Station> stations = new LinkedHashSet<>();
        for (Section section : sections) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }
        return stations;
    }
}
