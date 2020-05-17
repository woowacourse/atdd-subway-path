package wooteco.subway.admin.domain;

import java.util.Arrays;

public enum EdgeType {
    DURATION("duration"),
    DISTANCE("distance");

    private final String edgeValue;

    EdgeType(String edgeValue) {
        this.edgeValue = edgeValue;
    }

    public static EdgeType of(String type) {
        return Arrays.stream(values())
                .filter(edgeType -> type.equals(edgeType.getEdgeValue()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 EdgeType 입니다."));
    }

    public String getEdgeValue() {
        return edgeValue;
    }
}

