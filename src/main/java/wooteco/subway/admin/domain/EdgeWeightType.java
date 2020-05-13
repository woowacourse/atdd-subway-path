package wooteco.subway.admin.domain;

public enum EdgeWeightType {
    DISTANCE("distance",
        new SubwayRouteFactory(((graph, edge) -> graph.setEdgeWeight(edge, edge.getDistance())))),
    DURATION("duration",
        new SubwayRouteFactory(((graph, edge) -> graph.setEdgeWeight(edge, edge.getDuration()))));

    private final String name;
    private final SubwayRouteFactory factory;

    EdgeWeightType(String name, SubwayRouteFactory factory) {
        this.name = name;
        this.factory = factory;
    }

    public String getName() {
        return name;
    }

    public SubwayRouteFactory getFactory() {
        return factory;
    }
}
