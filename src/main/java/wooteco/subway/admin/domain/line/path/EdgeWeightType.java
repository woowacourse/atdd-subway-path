package wooteco.subway.admin.domain.line.path;

import wooteco.subway.admin.domain.line.LineStations;

public enum EdgeWeightType {
    DISTANCE((graph, edge) -> graph.setEdgeWeight(edge, edge.getDistance())),
    DURATION((graph, edge) -> graph.setEdgeWeight(edge, edge.getDuration()));

    private final EdgeWeightStrategy edgeWeightStrategy;

    EdgeWeightType(EdgeWeightStrategy edgeWeightStrategy) {
        this.edgeWeightStrategy = edgeWeightStrategy;
    }

    public SubwayRoute findShortestPath(LineStations lineStations, Long departureId, Long arrivalId) {
        return lineStations.findShortestPath(edgeWeightStrategy, departureId, arrivalId);
    }
}
