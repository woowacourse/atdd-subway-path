package wooteco.subway.path.domain;

import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayMap {
    private final WeightedGraph<Station, DefaultWeightedEdge> subwayMap
            = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final DijkstraShortestPath map = new DijkstraShortestPath(subwayMap);

    public SubwayMap(Sections sections) {
        initSubwayMap(sections);
    }

    private void initSubwayMap(Sections sections) {
        addAllStations(sections.getStations());
        addAllSectionInfo(sections.getSections());
    }

    private void addAllSectionInfo(List<Section> sections) {
        sections.forEach(section -> {
            int distance = section.getDistance();
            subwayMap.setEdgeWeight(connectSection(section), distance);
        });
    }

    private DefaultWeightedEdge connectSection(Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        return subwayMap.addEdge(upStation, downStation);
    }

    private void addAllStations(List<Station> stations) {
        stations.forEach(subwayMap::addVertex);
    }

    public double getShortestPath(Station source, Station target) {
        return map.getPathWeight(source, target);
    }

    public List<Station> getStationsOnPath(Station source, Station target) {
        return map.getPath(source, target).getVertexList();
    }
}
