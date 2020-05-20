package wooteco.subway.admin.domain.graph.jgraph;

import org.springframework.stereotype.Component;
import wooteco.subway.admin.domain.Edge;
import wooteco.subway.admin.domain.graph.Graph;
import wooteco.subway.admin.domain.graph.GraphStrategy;

import java.util.Set;
import java.util.function.Function;

@Component
public class SubwayJGraphStrategy implements GraphStrategy {
    @Override
    public Graph makeGraph(final Set<Edge> edges, final Function<Edge, Integer> edgeIntegerFunction) {
        return new SubwayJGraph(edges, edgeIntegerFunction);
    }
}
