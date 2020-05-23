package wooteco.subway.admin.dto;

import wooteco.subway.admin.domain.ShortestPath;

public class PathsResponse {
    private final PathResponse shortestDistanceResponse;
    private final PathResponse shortestDurationResponse;

    public PathsResponse(PathResponse shortestDistanceResponse, PathResponse shortestDurationResponse) {
        this.shortestDistanceResponse = shortestDistanceResponse;
        this.shortestDurationResponse = shortestDurationResponse;
    }

    public static PathsResponse of(ShortestPath shortestDistancePath, ShortestPath shortestDurationPath) {
        return new PathsResponse(PathResponse.of(shortestDistancePath), PathResponse.of(shortestDurationPath));
    }

    public PathResponse getShortestDistanceResponse() {
        return shortestDistanceResponse;
    }

    public PathResponse getShortestDurationResponse() {
        return shortestDurationResponse;
    }
}
