package wooteco.subway.admin.domain.line.path;

public enum EdgeWeightType {
    DISTANCE("distance", ((graph, edge) -> graph.setEdgeWeight(edge, edge.getDistance()))),
    DURATION("duration", ((graph, edge) -> graph.setEdgeWeight(edge, edge.getDuration())));

    private final String name;
    private final EdgeWeightStrategy edgeWeightStrategy;

    EdgeWeightType(String name, EdgeWeightStrategy edgeWeightStrategy) {
        this.name = name;
        this.edgeWeightStrategy = edgeWeightStrategy;
    }

    public String getName() {
        return name;
    }

    public EdgeWeightStrategy getEdgeWeightStrategy() {
        return edgeWeightStrategy;
    }
}
