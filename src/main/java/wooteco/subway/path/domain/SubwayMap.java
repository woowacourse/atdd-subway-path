package wooteco.subway.path.domain;

import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayMap {
    private final WeightedGraph<Station, DefaultWeightedEdge> subwayMap
            = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final DijkstraShortestPath map = new DijkstraShortestPath(subwayMap);

    public SubwayMap(List<Line> lines) {
        List<Sections> allSections = lines.stream()
                .map(Line::getSections)
                .collect(Collectors.toList());
        initSubwayMap(allSections);
    }

    private void initSubwayMap(List<Sections> allSections) {
        addAllStations(allSections);
        addAllSectionInfo(allSections);
    }

    private void addAllStations(List<Sections> allSections) {
        for (Sections sections : allSections) {
            sections.getStations().forEach(subwayMap::addVertex);
        }
    }

    private void addAllSectionInfo(List<Sections> allSections) {
        for (Sections sections : allSections) {
            sections.getSections().forEach(section -> {
                int distance = section.getDistance();
                subwayMap.setEdgeWeight(connectSection(section), distance);
            });
        }
    }

    private DefaultWeightedEdge connectSection(Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        return subwayMap.addEdge(upStation, downStation);
    }

    public int getShortestPath(Station source, Station target) {
        return (int) map.getPathWeight(source, target);
    }

    public List<Station> getStationsOnPath(Station source, Station target) {
        return map.getPath(source, target).getVertexList();
    }
}
