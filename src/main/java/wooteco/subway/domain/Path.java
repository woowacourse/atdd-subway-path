package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath;


    public Path(Lines lines) {
        this.shortestPath = new DijkstraShortestPath<>(multiGraph(lines));
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> multiGraph(Lines lines) {

        WeightedMultigraph<Station, DefaultWeightedEdge> graph
            = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        lines.allStations().forEach(graph::addVertex);
        lines.allSections().forEach(section ->
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance())
        );

        return graph;
    }

    public List<Station> shortestPath(Station sourceStation, Station targetStation) {
        return shortestPath.getPath(sourceStation, targetStation).getVertexList();
    }

    public int distance(Station sourceStation, Station targetStation) {
        return (int) shortestPath.getPath(sourceStation, targetStation).getWeight();
    }



    /*
        WeightedMultigraph<String, DefaultWeightedEdge> graph
            = new WeightedMultigraph(DefaultWeightedEdge.class);
    graph.addVertex("v1");
    graph.addVertex("v2");
    graph.addVertex("v3");
    graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
    graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
    graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

    DijkstraShortestPath dijkstraShortestPath
            = new DijkstraShortestPath(graph);
    List<String> shortestPath
            = dijkstraShortestPath.getPath("v3", "v1").getVertexList();

    assertThat(shortestPath.size()).isEqualTo(3);
     */
}
