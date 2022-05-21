package wooteco.subway.domain;

import wooteco.subway.domain.exception.UnreachablePathException;

public class PathSearcher {

    private final Graph graph;
    private final FareCalculator fareCalculator;

    public PathSearcher(Graph graph, FareCalculator fareCalculator) {
        this.graph = graph;
        this.fareCalculator = fareCalculator;
    }

    public PathSummary search(Station source, Station target) {
        if (source.equals(target)) {
            throw new UnreachablePathException(source, target);
        }

        Path path = graph.search(source, target);
        if (path.isEmpty()) {
            throw new UnreachablePathException(source, target);
        }
        int fare = fareCalculator.calculateFare(path);
        return new PathSummary(path, fare);
    }
}
