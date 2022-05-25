package wooteco.subway.exception;

public class FareAgeStrategyNotFoundException extends BusinessException {

    public FareAgeStrategyNotFoundException() {
        super("해당 나이에 대한 요금을 찾을 수 없습니다.");
    }
}
