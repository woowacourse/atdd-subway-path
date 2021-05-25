package wooteco.subway.exceptions;

import org.springframework.http.HttpStatus;

public enum SubWayException {

    DUPLICATE_EMAIL_EXCEPTION("중복된 이메일 입니다.", HttpStatus.BAD_REQUEST.value()),
    NOT_EXIST_STATION_EXCEPTION("존재하지 않는 역입니다.", HttpStatus.BAD_REQUEST.value()),
    NOT_CONNECT_STATION_EXCEPTION("연결되지 않은 역입니다.", HttpStatus.BAD_REQUEST.value());


    private final String message;
    private final int status;

    SubWayException(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String message() {
        return message;
    }

    public int status() {
        return status;
    }
}
