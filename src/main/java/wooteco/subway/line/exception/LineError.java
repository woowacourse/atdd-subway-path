package wooteco.subway.line.exception;

public enum LineError {
    NO_SECTION_MATCHED(400, "매치되는 구간이 없습니다"),
    NO_LINE_MATCHED(400, "매치되는 노선이 없습니다.");

    private int statusCode;
    private String message;

    LineError(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
