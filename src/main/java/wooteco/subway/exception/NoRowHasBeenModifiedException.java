package wooteco.subway.exception;

import org.springframework.dao.DataAccessException;

public class NoRowHasBeenModifiedException extends DataAccessException {
    public NoRowHasBeenModifiedException(String msg) {
        super(msg);
    }

    public NoRowHasBeenModifiedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
