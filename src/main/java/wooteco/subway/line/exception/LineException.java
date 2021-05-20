package wooteco.subway.line.exception;

public class LineException extends RuntimeException {
    private int statusCode;

    public LineException(LineError lineError) {
        super(lineError.getMessage());
        this.statusCode = lineError.getStatusCode();
    }

    public int getStatusCode() {
        return statusCode;
    }
}
