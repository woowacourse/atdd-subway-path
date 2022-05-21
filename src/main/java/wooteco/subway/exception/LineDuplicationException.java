package wooteco.subway.exception;

public class LineDuplicationException extends IllegalArgumentException{

    public LineDuplicationException(String message) {
        super(message);
    }
}
