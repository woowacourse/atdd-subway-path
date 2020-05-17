package wooteco.subway.admin.path.service.dto;

public class PathInfoResponse {

	private PathResponse shortestDistancePath;
	private PathResponse shortestDurationPath;

	public PathInfoResponse() {
	}

	public PathInfoResponse(PathResponse shortestDistancePath, PathResponse shortestDurationPath) {
		this.shortestDistancePath = shortestDistancePath;
		this.shortestDurationPath = shortestDurationPath;
	}

	public PathResponse getShortestDistancePath() {
		return shortestDistancePath;
	}

	public PathResponse getShortestDurationPath() {
		return shortestDurationPath;
	}
}
