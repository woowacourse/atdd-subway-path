package wooteco.subway.Validator;

import wooteco.subway.exception.DataNotExistException;

public class Validator {

    public static void checkNull(Object value) {
        if (value == null) {
            throw new DataNotExistException("값이 비어있거나 null일 수 없습니다.");
        }
    }
}
