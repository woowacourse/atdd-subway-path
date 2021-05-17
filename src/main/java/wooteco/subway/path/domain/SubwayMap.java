package wooteco.subway.path.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.section.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayMap {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap;

    public SubwayMap(WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap) {
        this.subwayMap = subwayMap;
    }

    public void addSection(Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        boolean isUpStationSaved = subwayMap.addVertex(upStation);
        boolean isDownStationSaved = subwayMap.addVertex(downStation);
        if (!isUpStationSaved && !isDownStationSaved) {
            return;
        }
        DefaultWeightedEdge defaultWeightedEdge = subwayMap.addEdge(upStation, downStation);
        subwayMap.setEdgeWeight(defaultWeightedEdge, section.getDistance());
    }

    public List<Station> findShortestPath(Station startStation, Station endStation) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(subwayMap);
        return dijkstraShortestPath.getPath(startStation, endStation)
                .getVertexList();
    }
}
