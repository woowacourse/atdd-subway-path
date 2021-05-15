package wooteco.subway.auth.exception;

public class MemberException extends RuntimeException{
    private final String message;

    public MemberException(String message) {
        this.message = message;
    }

}
