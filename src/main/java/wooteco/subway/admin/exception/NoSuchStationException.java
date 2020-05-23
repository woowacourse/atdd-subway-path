package wooteco.subway.admin.exception;

import java.util.NoSuchElementException;

public class NoSuchStationException extends NoSuchElementException {
	public NoSuchStationException() {
		super("해당역의 정보가 존재하지 않습니다.");
	}
}
