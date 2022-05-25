package wooteco.subway.domain.age;

public class BabyDiscountPolicy implements DiscountByAgePolicy {

    @Override
    public int apply(int fare) {
        return 0;
    }
}
