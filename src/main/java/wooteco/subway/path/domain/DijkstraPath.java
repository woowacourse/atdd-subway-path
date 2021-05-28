package wooteco.subway.path.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class DijkstraPath implements ShortestPath {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath;

    public DijkstraPath(Lines lines) {
        this.subwayMap = subwayMap(lines);
        this.shortestPath = shortestPath(subwayMap);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap(Lines lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        lines.allStations()
                .forEach(subwayMap::addVertex);
        lines.allSections()
                .forEach(section -> subwayMap.setEdgeWeight(
                        subwayMap.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance()));

        return subwayMap;
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath(WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap) {
        return new DijkstraShortestPath<>(subwayMap);
    }

    @Override
    public List<Station> find(Station source, Station target) {
        return shortestPath.getPath(source, target).getVertexList();
    }

    @Override
    public int calculate(Station source, Station target) {
        return (int) shortestPath.getPath(source, target).getWeight();
    }
}
