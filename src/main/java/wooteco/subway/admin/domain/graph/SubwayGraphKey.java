package wooteco.subway.admin.domain.graph;

import wooteco.subway.admin.domain.Edge;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum SubwayGraphKey {
    DISTANCE("distance", Edge::getDistance),
    DURATION("duration", Edge::getDuration);

    private final String symbol;
    private final Function<Edge, Integer> edgeIntegerFunction;

    SubwayGraphKey(final String symbol, final Function<Edge, Integer> edgeIntegerFunction) {
        this.symbol = symbol;
        this.edgeIntegerFunction = edgeIntegerFunction;
    }

    public static SubwayGraphKey of(String key) {
        return Arrays.stream(values())
                .filter(subwayGraphKey -> subwayGraphKey.symbol.equals(key))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static Map<SubwayGraphKey, Graph> makeGraph(Set<Edge> edges, GraphStrategy graphStrategy) {
        return Arrays.stream(values())
                .collect(Collectors.toMap(
                        key -> key,
                        key -> graphStrategy.makeGraph(edges, key.edgeIntegerFunction),
                        (oldOne, newOne) -> newOne,
                        () -> new EnumMap<>(SubwayGraphKey.class)
                ));
    }
}