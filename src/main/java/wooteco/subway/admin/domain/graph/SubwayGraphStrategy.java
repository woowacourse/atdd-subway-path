package wooteco.subway.admin.domain.graph;

import org.springframework.stereotype.Component;
import wooteco.subway.admin.domain.Edge;

import java.util.Set;
import java.util.function.Function;

@Component
public class SubwayGraphStrategy implements GraphStrategy {
    @Override
    public Graph makeGraph(final Set<Edge> edges, final Function<Edge, Integer> edgeIntegerFunction) {
        return new SubwayGraph(edges, edgeIntegerFunction);
    }
}
