package wooteco.subway.admin.domain;

import org.jgrapht.GraphPath;
import wooteco.subway.admin.exception.InvalidPathTypeException;

import java.util.Arrays;
import java.util.function.Function;

public enum PathType {
    DISTANCE(LineStation::getDistance, GraphPath::getWeight, graphPath -> {
        return graphPath.getEdgeList().stream()
                .mapToDouble(LineStationEdge::getDuration)
                .sum();
    }),
    DURATION(LineStation::getDuration, graphPath -> {
        return graphPath.getEdgeList().stream()
                .mapToDouble(LineStationEdge::getDistance)
                .sum();
    }, GraphPath::getWeight);

    private final Function<LineStation, Integer> strategy;
    private final Function<GraphPath<Station, LineStationEdge>, Double> distanceStrategy;
    private final Function<GraphPath<Station, LineStationEdge>, Double> durationStrategy;

    PathType(Function<LineStation, Integer> strategy, Function<GraphPath<Station, LineStationEdge>, Double> distanceStrategy, Function<GraphPath<Station, LineStationEdge>, Double> durationStrategy) {
        this.strategy = strategy;
        this.distanceStrategy = distanceStrategy;
        this.durationStrategy = durationStrategy;
    }

    public static PathType of(String input) {
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(input))
                .findAny()
                .orElseThrow(InvalidPathTypeException::new);
    }

    public int calculateDistance(GraphPath<Station, LineStationEdge> graphPath) {
        return distanceStrategy.apply(graphPath)
                .intValue();
    }

    public int calculateDuration(GraphPath<Station, LineStationEdge> graphPath) {
        return durationStrategy.apply(graphPath)
                .intValue();
    }

    public Function<LineStation, Integer> getStrategy() {
        return strategy;
    }
}
