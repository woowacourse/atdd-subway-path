package wooteco.subway.admin.exception;

public class NotFoundPathException extends BusinessException {

    public NotFoundPathException(Long source, Long target) {
        super(source + "에서 " + target + "으로 가는 경로가 존재하지 않습니다.");
    }
}
