package wooteco.subway.domain.farepolicy;

@FunctionalInterface
public interface FarePolicy {

    int DEDUCTED_AMOUNT = 350;

    int calculate(int basicFare);
}
