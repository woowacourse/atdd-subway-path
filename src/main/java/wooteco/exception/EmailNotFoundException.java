package wooteco.exception;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException(String s) {
        super(s);
    }
}
