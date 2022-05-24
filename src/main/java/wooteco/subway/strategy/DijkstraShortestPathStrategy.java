package wooteco.subway.strategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Lines;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;

@Component
public class DijkstraShortestPathStrategy implements DijkstraPathStrategy {

    @Override
    public Path getPath(Lines lines, Station source, Station target) {
        Sections sections = lines.getAllSections();
        validateExistInSections(sections, source);
        validateExistInSections(sections, target);

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = getDijkstraShortestPath(sections);
        GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(source, target);

        if (graphPath == null) {
            throw new NoSuchElementException("이동할 수 있는 경로가 없습니다.");
        }

        List<Station> visitStations = getVisitStations(graphPath);
        Lines visitLines = getVisitLines(graphPath, lines);
        int distance = getDistance(graphPath);

        return new Path(visitStations, visitLines, distance);
    }

    private static DijkstraShortestPath<Station, DefaultWeightedEdge> getDijkstraShortestPath(
            Sections sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Station station : sections.getStations()) {
            graph.addVertex(station);
        }
        sections.forEach(section -> graph.setEdgeWeight(
                graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance())
        );

        return new DijkstraShortestPath<>(graph);
    }

    private static List<Station> getVisitStations(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        return graphPath.getVertexList().stream()
                .map(station -> new Station(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }

    private Lines getVisitLines(GraphPath<Station, DefaultWeightedEdge> graphPath, Lines lines) {
        Set<Line> visitLine = new HashSet<>();

        List<Station> stations = graphPath.getVertexList();
        for (int i = 0; i < stations.size() - 1; i++) {
            Station upStation = stations.get(i);
            Station downStation = stations.get(i + 1);

            Line line = lines.getLineByMinDistance(upStation, downStation);
            visitLine.add(line);
        }

        return new Lines(new ArrayList<>(visitLine));
    }

    private static int getDistance(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        return (int) graphPath.getWeight();
    }

    private static void validateExistInSections(Sections sections, Station station) {
        if (!sections.getStations().contains(station)) {
            throw new NoSuchElementException("이동할 수 있는 경로가 없습니다.");
        }
    }
}
