package wooteco.subway.util;

import java.util.List;

public class GraphPathResponse {
    private final List<Long> vertexList;
    private final List<Edge> edgeList;
    private final double weight;

    public GraphPathResponse(List<Long> vertexList, List<Edge> edgeList, double weight) {
        this.vertexList = vertexList;
        this.edgeList = edgeList;
        this.weight = weight;
    }

    public List<Long> getVertexList() {
        return vertexList;
    }

    public List<Edge> getEdgeList() {
        return edgeList;
    }

    public double getWeight() {
        return weight;
    }
}
