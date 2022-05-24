package wooteco.subway.domain;

import java.util.List;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.fare.discountstrategy.AdultDiscountStrategy;
import wooteco.subway.domain.fare.discountstrategy.ChildDiscountStrategy;
import wooteco.subway.domain.fare.discountstrategy.DiscountStrategy;
import wooteco.subway.domain.fare.discountstrategy.FreeDiscountStrategy;
import wooteco.subway.domain.fare.discountstrategy.TeenagerDiscountStrategy;

public class Path {

    private static final String AGE_LOWER_BOUND_ERROR = "나이는 한살보다 작을 수 없습니다.";
    private final List<Station> stations;
    private final int distance;
    private final int extraFare;

    public Path(List<Station> stations, int distance, int extraFare) {
        this.stations = stations;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public int calculateFare(int age) {
        validateAge(age);

        DiscountStrategy discountStrategy = new AdultDiscountStrategy();

        if (age < 6 || age >= 65) {
            discountStrategy = new FreeDiscountStrategy();
        }
        if (age < 13) {
            discountStrategy = new ChildDiscountStrategy();
        }
        if (age < 19) {
            discountStrategy = new TeenagerDiscountStrategy();
        }

        return new Fare(distance, discountStrategy).calculate(extraFare);
    }

    private void validateAge(int age) {
        if (age < 1) {
            throw new IllegalArgumentException(AGE_LOWER_BOUND_ERROR);
        }
    }

    public List<Station> getStations() {
        return List.copyOf(stations);
    }

    public int getDistance() {
        return distance;
    }
}
