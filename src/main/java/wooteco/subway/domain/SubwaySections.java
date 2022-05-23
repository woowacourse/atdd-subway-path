package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwaySections {

    private static final String UNREGISTERED_SECTION_STATION = "노선에 등록되지 않은 지하철역입니다.";
    private static final String NOT_EXIST_PATH = "경로가 존재하지 않습니다.";

    private final List<Section> sections;
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public SubwaySections(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        fillVertex(sections);
        fillEdge(sections);
    }

    private void fillVertex(List<Section> sections) {
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
        }
    }

    private void fillEdge(List<Section> sections) {
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    public Path getShortestPath(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> shortestPath = getDijkstraShortestPath(source, target);
        validateExistPath(shortestPath);

        return new Path(shortestPath.getVertexList(), (int) shortestPath.getWeight(),
                getExtraFare(shortestPath.getVertexList()));
    }

    private int getExtraFare(List<Station> stations) {
        List<Integer> extraFares = getPassingLines(stations)
                .stream()
                .map(Line::getExtraFare)
                .collect(Collectors.toList());
        return Collections.max(extraFares);
    }

    private Set<Line> getPassingLines(List<Station> stations) {
        Set<Line> lines = new HashSet<>();
        for (int i = 0; i < (stations.size() - 1); i++) {
            Station upStation = stations.get(i);
            Station downStation = stations.get(i + 1);
            lines.add(searchSectionLine(upStation, downStation));
        }
        return lines;
    }

    private Line searchSectionLine(Station upStation, Station downStation) {
        Section foundSection = sections.stream()
                .filter(section -> section.existsStation(upStation) && section.existsStation(downStation))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("일치하는 구간이 없습니다."));
        return foundSection.getLine();
    }

    private GraphPath<Station, DefaultWeightedEdge> getDijkstraShortestPath(Station source, Station target) {
        try {
            return new DijkstraShortestPath<>(graph).getPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(UNREGISTERED_SECTION_STATION);
        }
    }

    private void validateExistPath(GraphPath<Station, DefaultWeightedEdge> shortestPath) {
        if (Objects.isNull(shortestPath)) {
            throw new IllegalArgumentException(NOT_EXIST_PATH);
        }
    }
}
