package wooteco.subway.admin.dto;

import java.util.Map;

import wooteco.subway.admin.domain.line.path.EdgeWeightType;

public class PathResponses {
    private Map<EdgeWeightType, PathResponse> response;

    private PathResponses() {
    }

    public PathResponses(Map<EdgeWeightType, PathResponse> response) {
        this.response = response;
    }

    public Map<EdgeWeightType, PathResponse> getResponse() {
        return response;
    }
}
