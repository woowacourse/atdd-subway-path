package wooteco.subway.exception;

public class VertexNotFoundException extends RuntimeException{
    private static final String MESSAGE = "요청한 역으로 경로 탐색에 실패했습니다.";

    public VertexNotFoundException() {
        super(MESSAGE);
    }

    public VertexNotFoundException(String message) {
        super(message);
    }

    public VertexNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}