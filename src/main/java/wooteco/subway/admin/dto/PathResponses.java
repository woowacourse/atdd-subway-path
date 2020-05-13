package wooteco.subway.admin.dto;

import java.util.Map;

public class PathResponses {
    private Map<String, PathResponse> response;

    private PathResponses() {
    }

    public PathResponses(Map<String, PathResponse> response) {
        this.response = response;
    }

    public Map<String, PathResponse> getResponse() {
        return response;
    }
}
