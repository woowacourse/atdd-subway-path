package wooteco.subway.admin.domain.exception;

import java.util.NoSuchElementException;

public class NoSuchStationException extends NoSuchElementException {
    private static final String MESSAGE = "역을 찾을 수 없습니다.";

    public NoSuchStationException() {
        super(MESSAGE);
    }
}
