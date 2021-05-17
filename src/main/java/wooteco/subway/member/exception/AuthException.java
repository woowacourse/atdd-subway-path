package wooteco.subway.member.exception;

import wooteco.subway.member.exception.message.AuthErrorMessage;
import wooteco.subway.member.exception.message.ErrorMessage;

public class AuthException extends HttpException{

    public AuthException(AuthErrorMessage errorInfo) {
        super(errorInfo.getHttpStatus(), new ErrorMessage(errorInfo.getMessage()));
    }
}
