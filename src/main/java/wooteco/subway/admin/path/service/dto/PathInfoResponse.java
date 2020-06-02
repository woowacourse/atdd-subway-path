package wooteco.subway.admin.path.service.dto;

import java.util.List;

public class PathInfoResponse {

    private static final int SHORTEST_DISTANCE_PATH = 0;
    private static final int SHORTEST_DURATION_PATH = 1;

    private PathResponse shortestDistancePath;
    private PathResponse shortestDurationPath;

    public PathInfoResponse() {
    }

    public PathInfoResponse(PathResponse shortestDistancePath, PathResponse shortestDurationPath) {
        this.shortestDistancePath = shortestDistancePath;
        this.shortestDurationPath = shortestDurationPath;
    }

    public static PathInfoResponse of(List<PathResponse> responses) {
        return new PathInfoResponse(responses.get(SHORTEST_DISTANCE_PATH), responses.get(SHORTEST_DURATION_PATH));
    }

    public PathResponse getShortestDistancePath() {
        return shortestDistancePath;
    }

    public PathResponse getShortestDurationPath() {
        return shortestDurationPath;
    }

}
