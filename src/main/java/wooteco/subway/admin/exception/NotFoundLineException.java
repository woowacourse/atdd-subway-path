package wooteco.subway.admin.exception;

import org.springframework.dao.DataAccessException;

public class NotFoundLineException extends DataAccessException {

    public NotFoundLineException() {
        super("line을 찾을 수 없습니다");
    }

    public NotFoundLineException(Long id) {
        super(id + "에 해당하는 line을 찾을 수 없습니다");
    }
}
