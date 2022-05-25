package wooteco.subway.domain.age;

public class BabyDiscountPolicy implements DiscountByAgePolicy {

    @Override
    public int apply(final int fare) {
        return 0;
    }
}
