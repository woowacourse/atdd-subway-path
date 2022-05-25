package wooteco.subway.domain.policy;

public interface DiscountPolicy {

    int calculateDiscountFare(int fare);

    boolean checkAgeRange(int age);
}
