package wooteco.subway.domain.fare;

public interface DiscountStrategy {
    int discount(int totalFare, int age);
}
