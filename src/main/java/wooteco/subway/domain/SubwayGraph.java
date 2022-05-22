package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayGraph {

    private final DijkstraShortestPath<Station, LineWeightedEdge> subwayMap;

    public SubwayGraph(List<Section> sections) {
        this.subwayMap = create(new ArrayList<>(sections));
    }

    public List<Station> getShortestRoute(Station source, Station target) {
        GraphPath<Station, LineWeightedEdge> result = subwayMap.getPath(source, target);
        validateRoute(result);
        return result.getVertexList();
    }

    public int getShortestDistance(Station source, Station target) {
        GraphPath<Station, LineWeightedEdge> path = this.subwayMap.getPath(source, target);
        return (int) path.getWeight();
    }

    public SubwayFare getFare(Station source, Station target) {
        return new SubwayFare(subwayMap.getPath(source, target));
    }

    private DijkstraShortestPath<Station, LineWeightedEdge> create(List<Section> sections) {
        WeightedMultigraph<Station, LineWeightedEdge> subwayMap =
                new WeightedMultigraph<>(LineWeightedEdge.class);
        for (Section section : sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            int distance = section.getDistance();
            Line line = section.getLine();

            subwayMap.addVertex(upStation);
            subwayMap.addVertex(downStation);
            subwayMap.addEdge(upStation, downStation, new LineWeightedEdge(line, distance));
        }
        return new DijkstraShortestPath<>(subwayMap);
    }

    private void validateRoute(GraphPath<Station, LineWeightedEdge> route) {
        if (route == null) {
            throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
        }
    }

}
