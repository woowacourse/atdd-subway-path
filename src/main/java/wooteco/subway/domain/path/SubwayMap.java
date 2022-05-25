package wooteco.subway.domain.path;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.Section;
import wooteco.subway.domain.station.Station;

public class SubwayMap {

    private final DijkstraShortestPath<Station, LineWeightedEdge> subwayMap;

    public SubwayMap(List<Section> sections) {
        this.subwayMap = create(new ArrayList<>(sections));
    }

    public ShortestPath getShortestPath(Station source, Station target) {
        return new ShortestPath(getPath(source, target));
    }

    private DijkstraShortestPath<Station, LineWeightedEdge> create(List<Section> sections) {
        return new DijkstraShortestPath<>(createSubwayMap(sections));
    }

    private Graph<Station, LineWeightedEdge> createSubwayMap(List<Section> sections) {
        Graph<Station, LineWeightedEdge> subwayMap = new WeightedMultigraph<>(LineWeightedEdge.class);
        for (Section section : sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            int distance = section.getDistance();
            Line line = section.getLine();

            subwayMap.addVertex(upStation);
            subwayMap.addVertex(downStation);
            subwayMap.addEdge(upStation, downStation, new LineWeightedEdge(line, distance));
        }
        return subwayMap;
    }

    private GraphPath<Station, LineWeightedEdge> getPath(Station source, Station target) {
        try {
            return subwayMap.getPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("존재하지 않는 역입니다.");
        }
    }

}
