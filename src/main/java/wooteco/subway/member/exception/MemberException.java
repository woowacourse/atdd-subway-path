package wooteco.subway.member.exception;

import wooteco.subway.auth.exception.AuthError;

public class MemberException extends RuntimeException {
    private int statusCode;

    public MemberException(MemberError authError) {
        super(authError.getMessage());
        this.statusCode = authError.getStatusCode();
    }

    public int getStatusCode() {
        return statusCode;
    }
}
