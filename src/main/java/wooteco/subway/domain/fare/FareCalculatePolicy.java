package wooteco.subway.domain.fare;

public interface FareCalculatePolicy {

    int calculate(int distance, int extraFare, int age);
}
