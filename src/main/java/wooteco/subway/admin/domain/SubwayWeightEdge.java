package wooteco.subway.admin.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.function.Function;

public class SubwayWeightEdge extends DefaultWeightedEdge {
    private final Edge edge;
    private final Function<Edge, Integer> edgeIntegerFunction;

    public SubwayWeightEdge(final Edge edge, final Function<Edge, Integer> edgeIntegerFunction) {
        this.edge = edge;
        this.edgeIntegerFunction = edgeIntegerFunction;
    }

    public Integer getValue(Function<Edge, Integer> edgeIntegerFunction) {
        return edgeIntegerFunction.apply(edge);
    }

    public Long getPreStationId() {
        return edge.getPreStationId();
    }

    public Long getStationId() {
        return edge.getStationId();
    }

    @Override
    protected double getWeight() {
        return edgeIntegerFunction.apply(edge);
    }
}
