package wooteco.subway.util;

public class GraphEdgeRequest {
    private final Long sourceVertex;
    private final Long targetVertex;
    private final int weight;

    public GraphEdgeRequest(Long sourceVertex, Long targetVertex, int weight) {
        this.sourceVertex = sourceVertex;
        this.targetVertex = targetVertex;
        this.weight = weight;
    }

    public Long getSourceVertex() {
        return sourceVertex;
    }

    public Long getTargetVertex() {
        return targetVertex;
    }

    public int getWeight() {
        return weight;
    }
}
