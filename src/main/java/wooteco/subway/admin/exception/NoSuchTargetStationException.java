package wooteco.subway.admin.exception;

import java.util.NoSuchElementException;

public class NoSuchTargetStationException extends NoSuchElementException {
	public NoSuchTargetStationException() {
		super("도착역의 정보가 존재하지 않습니다.");
	}
}
