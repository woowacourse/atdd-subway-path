package wooteco.subway.path.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class ShortestPath {
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public ShortestPath(List<Line> lines) {
        this.dijkstraShortestPath = new DijkstraShortestPath<>(initSubwayMap(lines));
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> initSubwayMap(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Line line : lines) {
            List<Station> stations = line.getStations();
            stations.forEach(subwayMap::addVertex);
            Sections sections = line.getSections();
            connectStation(subwayMap, sections);
        }
        return subwayMap;
    }

    private void connectStation(WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap, Sections sections) {
        for (Section section : sections.getSections()) {
            subwayMap.setEdgeWeight(subwayMap.addEdge(section.getUpStation(), section.getDownStation()),
                    section.getDistance());
        }
    }

    public List<Station> getPath(Station sourceStation, Station targetStation) {
        return dijkstraShortestPath.getPath(sourceStation, targetStation).getVertexList();
    }

    public int distance(Station sourceStation, Station targetStation) {
        return (int) dijkstraShortestPath.getPath(sourceStation, targetStation).getWeight();
    }
}
