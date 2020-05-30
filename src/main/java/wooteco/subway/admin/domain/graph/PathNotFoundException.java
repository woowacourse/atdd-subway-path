package wooteco.subway.admin.domain.graph;

public class PathNotFoundException extends RuntimeException {
	public final static String PATH_NOT_FOUND_MESSAGE = "출발역과 도착역의 경로가 없습니다.";
	public final static String STATION_NOT_FOUND_MESSAGE = "유효하지 않은 역을 입력했습니다.";

	public PathNotFoundException() {
		super();
	}

	public PathNotFoundException(String message) {
		super(message);
	}
}
