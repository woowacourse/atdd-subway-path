package wooteco.subway.domain.fare;

public interface DiscountStrategy {

    int discount(int fare);

}
