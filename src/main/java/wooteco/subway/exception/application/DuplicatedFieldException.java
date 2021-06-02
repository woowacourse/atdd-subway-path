package wooteco.subway.exception.application;

import wooteco.subway.exception.web.BadRequestException;

public class DuplicatedFieldException extends BadRequestException {

    public DuplicatedFieldException(String description) {
        super(String.format("중복 값이 있습니다. (%s)", description));
    }
}
