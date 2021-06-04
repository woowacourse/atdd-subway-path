package wooteco.subway.exception.application;

import wooteco.subway.exception.web.NotFoundException;

public class ObjectNotFoundException extends NotFoundException {

    public ObjectNotFoundException(String description) {
        super(String.format("객체를 찾는 데 실패했습니다. (%s)", description));
    }
}
