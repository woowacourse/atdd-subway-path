package wooteco.subway.admin.domain.graph;

import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.admin.domain.Edge;

import java.util.function.Function;

public class SubwayWeightEdge extends DefaultWeightedEdge implements WeightEdge {
    private final Edge edge;
    private final Function<Edge, Integer> edgeIntegerFunction;

    public SubwayWeightEdge(final Edge edge, final Function<Edge, Integer> edgeIntegerFunction) {
        this.edge = edge;
        this.edgeIntegerFunction = edgeIntegerFunction;
    }

    @Override
    public Integer getValue(Function<Edge, Integer> edgeIntegerFunction) {
        return edgeIntegerFunction.apply(edge);
    }

    @Override
    public Long getPreStationId() {
        return edge.getPreStationId();
    }

    @Override
    public Long getStationId() {
        return edge.getStationId();
    }

    @Override
    protected double getWeight() {
        return edgeIntegerFunction.apply(edge);
    }
}
