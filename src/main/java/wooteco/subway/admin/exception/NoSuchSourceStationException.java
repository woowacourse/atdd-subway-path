package wooteco.subway.admin.exception;

import java.util.NoSuchElementException;

public class NoSuchSourceStationException extends NoSuchElementException {
	public NoSuchSourceStationException() {
		super("출발역의 정보가 존재하지 않습니다.");
	}
}
