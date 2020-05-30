package wooteco.subway.admin.exception;

public class DisconnectedPathException extends IllegalArgumentException {
	public DisconnectedPathException() {
		super("출발역과 도착역이 연결되어있지 않습니다. 역을 다시 지정해주세요.");
	}
}
