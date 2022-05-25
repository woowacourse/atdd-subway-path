package wooteco.subway.domain.age;

public class AdultDiscountPolicy implements DiscountByAgePolicy {

    @Override
    public int apply(int fare) {
        return fare;
    }
}
