package wooteco.subway.domain;

import java.util.List;
import wooteco.subway.domain.farerule.AgeDiscount;
import wooteco.subway.domain.farerule.DistanceCharge;

public class Path {

    private static final int BASIC_FARE = 1250;

    private List<Station> stations;
    private int lineExtraFare;
    private int distance;

    public Path(List<Station> stations, int extraFare, int distance) {
        this.stations = stations;
        this.lineExtraFare = extraFare;
        this.distance = distance;
    }

    public int finalFare(int age) {
        int fare = BASIC_FARE + lineExtraFare + DistanceCharge.calculateDistanceCharge(distance);
        int discount = AgeDiscount.calculateAgeDiscount(age, fare);
        return fare - discount;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getLineExtraFare() {
        return lineExtraFare;
    }
}
