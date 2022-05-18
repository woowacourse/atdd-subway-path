package wooteco.subway.exception;

import java.util.NoSuchElementException;

public class NoSuchPathException extends NoSuchElementException {

    public NoSuchPathException(Long sourceId, Long targetId) {
        super(sourceId + " -> " + targetId + "의 경로가 존재하지 않습니다.");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
