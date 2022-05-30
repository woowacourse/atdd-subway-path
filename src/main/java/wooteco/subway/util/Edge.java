package wooteco.subway.util;

import java.util.List;

public class Edge {
    private final Long sourceVertex;
    private final Long targetVertex;

    public Edge(List<Long> it) {
        this.sourceVertex = it.get(0);
        this.targetVertex = it.get(1);
    }

    public Long getSourceVertex() {
        return sourceVertex;
    }

    public Long getTargetVertex() {
        return targetVertex;
    }
}
