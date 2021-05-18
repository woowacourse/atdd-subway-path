package wooteco.subway.path.domain;

import java.util.List;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

public class DistanceMap {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> map = new WeightedMultigraph<>(DefaultWeightedEdge.class);

    public DistanceMap() {
    }

    public ShortestPathMap toShortestPathMap(List<Line> lines) {
        connectMap(lines);
        return new ShortestPathMap(map);
    }

    private void connectMap(List<Line> lines) {
        for (Line line : lines) {
            createVertex(line.getStations());
            createEdge(line.getSections());
        }
    }

    private void createVertex(List<Station> stations) {
        for (Station station : stations) {
            map.addVertex(station);
        }
    }

    private void createEdge(Sections sections) {
        for (Section section : sections.getSections()) {
            map.setEdgeWeight(map.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

}
