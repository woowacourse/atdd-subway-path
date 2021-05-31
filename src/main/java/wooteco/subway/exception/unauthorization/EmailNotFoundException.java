package wooteco.subway.exception.unauthorization;

import wooteco.subway.exception.notfound.NotFoundException;

public class EmailNotFoundException extends UnAuthorizationException {
    private static final String MESSAGE = "이메일이 존재하지 않습니다.";

    public EmailNotFoundException() {
        super(MESSAGE);
    }

    public EmailNotFoundException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
