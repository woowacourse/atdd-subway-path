package wooteco.subway.domain.pathfinder;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

@Component
public class JgraphtPathFinder implements PathFinder {

    @Override
    public Path findShortest(List<Line> lines, Station source, Station target) {
        WeightedMultigraph<Station, SectionAsWeightedEdge> graph =
                new WeightedMultigraph<>(SectionAsWeightedEdge.class);

        addAllStationsAsVertex(lines, graph);
        addAllSectionsAsEdge(lines, graph);

        GraphPath<Station, SectionAsWeightedEdge> path = new DijkstraShortestPath<>(graph).getPath(source, target);

        return new Path(path.getVertexList(), findMostExpensiveExtraFare(path), (int) path.getWeight());
    }

    private int findMostExpensiveExtraFare(GraphPath<Station, SectionAsWeightedEdge> path) {
        return path.getEdgeList().stream()
                .max(Comparator.comparing(SectionAsWeightedEdge::getExtraFare))
                .get()
                .getExtraFare();
    }

    private List<Station> allStationsOnLines(List<Line> lines) {
        return lines.stream().flatMap(line -> line.getStations().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    private void addAllStationsAsVertex(List<Line> lines, WeightedMultigraph<Station, SectionAsWeightedEdge> graph) {
        for (Station station : allStationsOnLines(lines)) {
            graph.addVertex(station);
        }
    }

    private void addAllSectionsAsEdge(List<Line> lines, WeightedMultigraph<Station, SectionAsWeightedEdge> graph) {
        for (Line line : lines) {
            addSectionsInLineAsEdge(line, graph);
        }
    }

    private void addSectionsInLineAsEdge(Line line, WeightedMultigraph<Station, SectionAsWeightedEdge> graph) {
        for (Section section : line.getSections()) {
            graph.addEdge(section.getUp(), section.getDown(),
                    new SectionAsWeightedEdge(line.getExtraFare(), section.getDistance()));
        }
    }
}
