package wooteco.subway.exception;

import wooteco.subway.exception.datanotfound.DataNotFoundException;

public class DeleteSectionException extends DataNotFoundException {

    public DeleteSectionException(String message) {
        super(message);
    }
}
