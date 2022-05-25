package wooteco.subway.domain.fare;

public class FareDiscountPolicyFactory {

    public static FareDiscountPolicy create(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("나이는 음수일 수 없습니다.");
        }
        if (13 <= age && age < 19) {
            return new TeenagerDiscountPolicy();
        }
        if (6 <= age && age < 13) {
            return new ChildDiscountPolicy();
        }
        return new NoDiscountPolicy();
    }

}
