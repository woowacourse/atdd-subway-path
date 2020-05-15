package wooteco.subway.admin.exception;

public class LineNotFoundException extends RuntimeException {

    public static final String LINE_NOT_FOUND_EXCEPTION_MESSAGE = "존재하지 않는 노선입니다.";

    public LineNotFoundException(){
        super(LINE_NOT_FOUND_EXCEPTION_MESSAGE);
    }
}
