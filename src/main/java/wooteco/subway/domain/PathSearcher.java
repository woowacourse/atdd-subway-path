package wooteco.subway.domain;

import java.util.List;

public class PathSearcher {

    private final Graph graph;
    private final FareCalculator fareCalculator;

    public PathSearcher(Graph graph, FareCalculator fareCalculator) {
        this.graph = graph;
        this.fareCalculator = fareCalculator;
    }

    public PathSummary find(Long source, Long target) {
        List<Station> path = graph.findPath(source, target);
        int distance = graph.findDistance(source, target);
        int fare = fareCalculator.findFare(distance);
        return new PathSummary(path, distance, fare);
    }
}
