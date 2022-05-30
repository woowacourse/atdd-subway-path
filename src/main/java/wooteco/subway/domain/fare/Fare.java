package wooteco.subway.domain.fare;

public interface Fare {

    int DEFAULT_FARE_AMOUNT = 1250;

    int calculateFare(int distance, int extraFare);

    boolean checkDistanceRange(int distance);
}
