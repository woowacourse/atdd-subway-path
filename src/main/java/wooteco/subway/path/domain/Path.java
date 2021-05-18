package wooteco.subway.path.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class Path {
    private final DijkstraShortestPath dijkstraShortestPath;

    public Path(List<Line> lines) {
        this.dijkstraShortestPath = new DijkstraShortestPath(initSubwayMap(lines));
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> initSubwayMap(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap
                = new WeightedMultigraph(DefaultWeightedEdge.class);
        for (Line line : lines) {
            List<Station> stations = line.getStations();
            stations.forEach(subwayMap::addVertex);
            Sections sections = line.getSections();
            connectStationsBySection(subwayMap, sections);
        }
        return subwayMap;
    }

    private void connectStationsBySection(WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap, Sections sections) {
        sections.getSections()
                .forEach(section -> subwayMap.setEdgeWeight(subwayMap.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance()));
    }

    public List<Station> getShortestPath(Station sourceStation, Station targetStation) {
        return dijkstraShortestPath.getPath(sourceStation, targetStation).getVertexList();
    }

    public int getShortestDistance(Station sourceStation, Station targetStation) {
        return (int) dijkstraShortestPath.getPathWeight(sourceStation, targetStation);
    }
}
