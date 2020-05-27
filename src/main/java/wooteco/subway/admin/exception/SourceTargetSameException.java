package wooteco.subway.admin.exception;

public class SourceTargetSameException extends RuntimeException {
    private static final String SOURCE_TARGET_SAME_EXCEPTION_MESSAGE = "출발역과 도착역이 같습니다";
    private static final String SOURCE_TARGET_SAME_EXCEPTION_FORMAT = "출발역과 도착역 이름이 %s로 같습니다";

    public SourceTargetSameException() {
        super(SOURCE_TARGET_SAME_EXCEPTION_MESSAGE);
    }

    public SourceTargetSameException(String name) {
        super(String.format(SOURCE_TARGET_SAME_EXCEPTION_FORMAT, name));
    }
}
