package wooteco.subway.domain.age;

public class KidsDiscountPolicy implements DiscountByAgePolicy {

    @Override
    public int apply(final int fare) {
        return (int) Math.ceil((fare - 350) * 0.5);
    }
}
