package wooteco.subway.domain;

public interface FarePolicy {

    int calculate(int distance, int highestExtraFare, int age);
}
