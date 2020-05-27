package wooteco.subway.admin.exception;

import java.util.NoSuchElementException;

public class CanNotCreateGraphException extends NoSuchElementException {
    private static final String MESSAGE = "그래프를 만들 수 없습니다.";

    public CanNotCreateGraphException() {
        super(MESSAGE);
    }
}
