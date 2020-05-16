package wooteco.subway.exception;

public class InvalidPathException extends IllegalArgumentException {
	public static final String DUPLICATE_DEPARTURE_AND_DESTINATION = "출발역과 도착역은 같을 수 없습니다";
	public static final String NOT_CONNECTED_PATH = "%s와 %s가 연결되어있지 않습니다.";
	public static final String NOT_EXIST_PATH_WEIGHT = "해당 경로 탐색 기준이 존재하지 않습니다.";
	public static final String NOT_BLANK = "출발역 혹은 도착역은 Null이 될 수 없습니다.";
	public static final String NOT_EXIST_STATION = "%s을 찾을 수 없습니다.";

	public InvalidPathException(String errorMessage) {
		super(errorMessage);
	}

	public InvalidPathException(String message, String source, String target) {
		this(String.format(message, source, target));
	}

	public InvalidPathException(String message, String station) {
		this(String.format(message, station));
	}
}
