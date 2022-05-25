package wooteco.subway.exception;

public class ExtraFareNotFoundException extends BusinessException {

    public ExtraFareNotFoundException() {
        super("노선의 최고 금액을 찾을 수 없습니다.");
    }
}
