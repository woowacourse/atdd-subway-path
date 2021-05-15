package wooteco.subway.exceptions;

import org.springframework.http.HttpStatus;

public enum SubWayException {

    DUPLICATE_EMAIL_EXCEPTION("중복된 이메일 입니다.", HttpStatus.BAD_REQUEST.value());

    private static final String ERROR = "[ERROR] ";
    private final String message;
    private final int status;

    SubWayException(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String message() {
        return ERROR + message;
    }

    public int status() {
        return status;
    }
}
