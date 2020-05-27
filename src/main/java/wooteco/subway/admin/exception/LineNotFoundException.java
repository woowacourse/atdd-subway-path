package wooteco.subway.admin.exception;

public class LineNotFoundException extends RuntimeException {

    public static final String LINE_NOT_FOUND_EXCEPTION_MESSAGE = "LINE_NOT_FOUND";

    public LineNotFoundException(){
        super(LINE_NOT_FOUND_EXCEPTION_MESSAGE);
    }
}
