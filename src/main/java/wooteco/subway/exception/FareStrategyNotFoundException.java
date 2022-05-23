package wooteco.subway.exception;

public class FareStrategyNotFoundException extends NotFoundException {

    public FareStrategyNotFoundException() {
        super("[ERROR] 요금 정책을 찾을 수 없습니다.");
    }
}
