package wooteco.subway.admin.domain.graph;

import wooteco.subway.admin.domain.Edge;

import java.util.Set;
import java.util.function.Function;

public interface GraphStrategy {
    Graph makeGraph(Set<Edge> edges, Function<Edge, Integer> edgeIntegerFunction);
}
