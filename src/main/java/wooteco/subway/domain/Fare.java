package wooteco.subway.domain;

import java.util.List;
import java.util.NoSuchElementException;
import wooteco.subway.domain.strategy.FareStrategy;
import wooteco.subway.domain.strategy.discount.DiscountStrategy;

public class Fare {

    private static final int BASIC_FARE = 1250;

    private final FareStrategy fareStrategy;
    private final DiscountStrategy discountStrategy;

    public Fare(FareStrategy fareStrategy, DiscountStrategy discountStrategy) {
        this.fareStrategy = fareStrategy;
        this.discountStrategy = discountStrategy;
    }


    public int calculateFare(int distance, int extraFare) {
        int fare = BASIC_FARE + fareStrategy.calculate(distance);
        return discountStrategy.calculate(fare + extraFare);
    }

    public int calculateMaxLineExtraFare(List<Line> lines) {
        return lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElseThrow(NoSuchElementException::new);
    }
}
