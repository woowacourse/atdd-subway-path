package wooteco.subway.exception;

public class NotExistLineException extends IllegalArgumentException{

    public NotExistLineException(String message) {
       super(message);
    }
}
