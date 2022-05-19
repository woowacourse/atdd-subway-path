package wooteco.subway.exception;

public class LineNotFoundException extends BusinessException {
    public LineNotFoundException(Long id) {
        super(String.format("%d 라인이 존재하지 않습니다", id));
    }
}
