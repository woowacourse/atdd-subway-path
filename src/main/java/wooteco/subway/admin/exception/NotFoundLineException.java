package wooteco.subway.admin.exception;

public class NotFoundLineException extends RuntimeException {

    public NotFoundLineException(Long id) {
        super(id + "에 해당하는 line을 찾을 수 없습니다");
    }
}
