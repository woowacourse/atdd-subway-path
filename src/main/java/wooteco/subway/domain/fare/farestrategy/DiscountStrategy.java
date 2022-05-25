package wooteco.subway.domain.fare.farestrategy;

public interface DiscountStrategy {
    long calculate(long fare);
}
