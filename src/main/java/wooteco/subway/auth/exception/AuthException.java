package wooteco.subway.auth.exception;

public class AuthException extends RuntimeException {
    private int statusCode;

    public AuthException(AuthError authError) {
        super(authError.getMessage());
        this.statusCode = authError.getStatusCode();
    }

    public int getStatusCode() {
        return statusCode;
    }
}
