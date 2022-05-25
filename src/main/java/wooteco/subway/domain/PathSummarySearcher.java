package wooteco.subway.domain;

import wooteco.subway.domain.exception.UnreachablePathException;

public class PathSummarySearcher {

    private final SubwayGraph subwayGraph;
    private final FareCalculator fareCalculator;

    public PathSummarySearcher(SubwayGraph subwayGraph, FareCalculator fareCalculator) {
        this.subwayGraph = subwayGraph;
        this.fareCalculator = fareCalculator;
    }

    public PathSummary search(Station source, Station target) {
        if (source.equals(target)) {
            throw new UnreachablePathException(source, target);
        }

        Path path = subwayGraph.search(source, target);
        if (path.isEmpty()) {
            throw new UnreachablePathException(source, target);
        }
        int fare = fareCalculator.calculateFare(path);
        return new PathSummary(path, fare);
    }
}
