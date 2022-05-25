package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.fare.SubwayFare;
import wooteco.subway.domain.path.ShortestPath;

public class SubwayGraph {

    private final DijkstraShortestPath<Station, LineWeightedEdge> subwayMap;

    public SubwayGraph(List<Section> sections) {
        this.subwayMap = create(new ArrayList<>(sections));
    }

    public ShortestPath getShortestPath(Station source, Station target) {
        return new ShortestPath(shortestPath(source, target));
    }

    public SubwayFare getFare(Station source, Station target) {
        return new SubwayFare(shortestPath(source, target));
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

    private GraphPath<Station, LineWeightedEdge> shortestPath(Station source, Station target) {
        try {
            return subwayMap.getPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("존재하지 않는 역입니다.");
        }
    }

}
