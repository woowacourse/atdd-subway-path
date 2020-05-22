package wooteco.subway.admin.exception;

public class LineNotFoundException extends NotFoundException {
    public LineNotFoundException(Long id) {
        super(String.format("%d를 가진 Line을 찾을 수 없습니다.", id));
    }
}
