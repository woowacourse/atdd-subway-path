package wooteco.subway.domain.path;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.fare.FareByAge;
import wooteco.subway.domain.fare.FareByDistance;
import wooteco.subway.domain.graph.ShortestPathEdge;
import wooteco.subway.domain.line.Lines;

public class PathFinder {

    public Path getPath(final GraphPath<Station, ShortestPathEdge> graph, final Lines lines, final int age) {
        List<Station> stations = graph.getVertexList();
        double distance = graph.getWeight();
        int fare = calculateFare(age, distance, getMaxExtraFare(graph, lines));
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
