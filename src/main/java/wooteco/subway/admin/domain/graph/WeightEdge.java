package wooteco.subway.admin.domain.graph;

import wooteco.subway.admin.domain.Edge;

import java.util.function.Function;

public interface WeightEdge {
    Integer getValue(Function<Edge, Integer> edgeIntegerFunction);

    Long getPreStationId();

    Long getStationId();
}
