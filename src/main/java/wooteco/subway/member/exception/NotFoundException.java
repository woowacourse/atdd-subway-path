package wooteco.subway.member.exception;

import wooteco.subway.member.exception.message.ErrorMessage;
import wooteco.subway.member.exception.message.NotFoundErrorMessage;

public class NotFoundException extends HttpException {

    public NotFoundException(NotFoundErrorMessage errorInfo) {
        super(errorInfo.getHttpStatus(), new ErrorMessage(errorInfo.getMessage()));
    }
}
