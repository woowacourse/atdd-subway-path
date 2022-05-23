package wooteco.subway.exception;

public class AgeDiscountPolicyNotFoundException extends NotFoundException {

    public AgeDiscountPolicyNotFoundException() {
        super("[ERROR] 할인 정책을 찾을 수 없습니다.");
    }
}
