package wooteco.subway.domain.fare;

public interface DiscountStrategy {

    int discount(Age age, int fare);

}
