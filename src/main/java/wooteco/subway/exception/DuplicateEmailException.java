package wooteco.subway.exception;

public class DuplicateEmailException extends RuntimeException {
    private static final String MESSAGE = "중복된 이메일 입니다. ";

    public DuplicateEmailException(String email) {
        super(MESSAGE + email);
    }
}
