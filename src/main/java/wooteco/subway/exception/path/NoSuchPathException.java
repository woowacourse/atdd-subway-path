package wooteco.subway.exception.path;

import wooteco.subway.exception.NoSuchRecordException;

public class NoSuchPathException extends NoSuchRecordException {

    private static final String MESSAGE = "경로가 존재하지 않습니다.";

    public NoSuchPathException() {
        super(MESSAGE);
    }
}

