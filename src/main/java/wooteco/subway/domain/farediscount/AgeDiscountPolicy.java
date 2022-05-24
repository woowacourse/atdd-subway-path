package wooteco.subway.domain.farediscount;

public abstract class AgeDiscountPolicy implements DiscountPolicy{

    @Override
    abstract public int apply(int price);
}
