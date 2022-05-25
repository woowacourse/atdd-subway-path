package wooteco.subway.exception;

public class FareDistanceStrategyNotFoundException extends BusinessException {

    public FareDistanceStrategyNotFoundException() {
        super("해당 거리에 대한 요금을 찾을 수 없습니다.");
    }
}
