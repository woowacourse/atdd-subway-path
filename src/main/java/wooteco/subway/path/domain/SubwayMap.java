package wooteco.subway.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayMap {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap;
    private final ShortestPath shortestPath;

    public SubwayMap(Lines lines) {
        this.subwayMap = make(lines);
        this.shortestPath = new ShortestPath(subwayMap);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> make(Lines lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap =
                new WeightedMultigraph<>(DefaultWeightedEdge.class);

        lines.allStations()
                .forEach(subwayMap::addVertex);
        lines.allSections()
                .forEach(section -> subwayMap.setEdgeWeight(subwayMap.addEdge(
                        section.getUpStation(), section.getDownStation()),
                        section.getDistance()));

        return subwayMap;
    }

    public List<Station> shortestPath(Station source, Station target) {
        return shortestPath.find(source, target);
    }

    public int distance(Station source, Station target) {
        return shortestPath.calculate(source, target);
    }
}
