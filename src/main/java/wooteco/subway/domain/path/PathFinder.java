package wooteco.subway.domain.path;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.fare.FareByAge;
import wooteco.subway.domain.fare.FareByDistance;
import wooteco.subway.domain.graph.ShortestPathEdge;
import wooteco.subway.domain.line.Lines;
import wooteco.subway.domain.secion.Section;
import wooteco.subway.domain.secion.Sections;
import wooteco.subway.exception.NotLinkPathException;

public class PathFinder {

    private final GraphPath<Station, ShortestPathEdge> shortestGraph;

    public PathFinder(final GraphPath<Station, ShortestPathEdge> shortestGraph) {
        this.shortestGraph = shortestGraph;
    }

    public static PathFinder init(final Sections sections, Station source, Station target) {
        WeightedMultigraph<Station, ShortestPathEdge> subwayGraph = new WeightedMultigraph<>(
                ShortestPathEdge.class);
        addStationsToVertex(sections, subwayGraph);
        addSectionsToEdge(sections, subwayGraph);
        return new PathFinder(graphResult(source, target, subwayGraph));
    }

    private static void addStationsToVertex(final Sections sections,
                                            final WeightedMultigraph<Station, ShortestPathEdge> subwayGraph) {
        for (Station station : sections.getStations()) {
            subwayGraph.addVertex(station);
        }
    }

    private static void addSectionsToEdge(final Sections sections,
                                          final WeightedMultigraph<Station, ShortestPathEdge> subwayGraph) {
        for (Section section : sections.getSections()) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            ShortestPathEdge edge = new ShortestPathEdge(section.getLineId(), section.getDistance());
            subwayGraph.addEdge(upStation, downStation, edge);
        }
    }

    private static GraphPath<Station, ShortestPathEdge> graphResult(final Station source, final Station target,
                                                                    WeightedMultigraph<Station, ShortestPathEdge> subwayGraph) {
        DijkstraShortestPath<Station, ShortestPathEdge> pathFinder = new DijkstraShortestPath<>(subwayGraph);
        GraphPath<Station, ShortestPathEdge> path = pathFinder.getPath(source, target);
        validateSourceToTargetLink(path);
        return path;
    }

    private static void validateSourceToTargetLink(final GraphPath<Station, ShortestPathEdge> path) {
        if (path == null) {
            throw new NotLinkPathException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
    }

    public Path getPath(final Lines lines, final int age) {
        List<Station> stations = shortestGraph.getVertexList();
        double distance = shortestGraph.getWeight();
        int fare = calculateFare(age, distance, getMaxExtraFare(shortestGraph, lines));
        return new Path(stations, distance, fare);
    }

    private int calculateFare(final int age, final double distance, final int maxExtraFare) {
        int distanceFare = FareByDistance.findFare(distance);
        return FareByAge.findFare(age, distanceFare + maxExtraFare);
    }

    private int getMaxExtraFare(final GraphPath<Station, ShortestPathEdge> graph, final Lines lines) {
        List<Long> lineIds = graph.getEdgeList().stream()
                .map(ShortestPathEdge::getLineId)
                .collect(Collectors.toList());

        return lines.findMaxExtraFare(lineIds);
    }
}
