package wooteco.subway.path;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

public class Path {

    private final Station sourceStation;
    private final Station targetStation;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> pathMap;

    public Path(Station sourceStation, Station targetStation,
        Sections sections) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.pathMap = createPathMap(sections);
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> createPathMap(Sections sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
            = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertexes(graph, sections);
        setEdgeWeights(graph, sections);

        return new DijkstraShortestPath<>(graph);
    }

    public List<Station> makePath() {
        return pathMap.getPath(sourceStation, targetStation).getVertexList();
    }

    public int totalDistance() {
        return (int) pathMap.getPath(sourceStation, targetStation).getWeight();
    }

    private void addVertexes(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
        Sections sections) {
        List<Station> stations = sections.getSections().stream()
            .map(section -> Arrays.asList(section.getUpStation(), section.getDownStation()))
            .flatMap(List::stream)
            .distinct()
            .collect(Collectors.toList());

        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void setEdgeWeights(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
        Sections sections) {
        for (Section section : sections.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
                section.getDistance());
        }
    }
}


