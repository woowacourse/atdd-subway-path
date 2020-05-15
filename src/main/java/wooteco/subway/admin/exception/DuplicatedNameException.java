package wooteco.subway.admin.exception;

public class DuplicatedNameException extends IllegalArgumentException {
    private static final String message = "중복된 이름(%s)이 이미 존재합니다.";

    public DuplicatedNameException(final String name) {
        super(String.format(message, name));
    }
}
