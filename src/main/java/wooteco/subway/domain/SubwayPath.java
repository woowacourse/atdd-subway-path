package wooteco.subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class SubwayPath {
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> subwayPath;

    private SubwayPath(DijkstraShortestPath<Station, DefaultWeightedEdge> subwayPath) {
        this.subwayPath = subwayPath;
    }

    public static SubwayPath of(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> subwayGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        sections.forEach(section -> {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();

            subwayGraph.addVertex(upStation);
            subwayGraph.addVertex(downStation);
            DefaultWeightedEdge edge = subwayGraph.addEdge(upStation, downStation);
            subwayGraph.setEdgeWeight(edge, section.getDistance());
        });

        return new SubwayPath(new DijkstraShortestPath<>(subwayGraph));
    }

    public List<Station> findShortestPath(Station fromStation, Station toStation) {
        return this.subwayPath.getPath(fromStation, toStation).getVertexList();
    }

    public int getTotalDistance(Station fromStation, Station toStation) {
        return (int) this.subwayPath.getPathWeight(fromStation, toStation);
    }
}
