package wooteco.subway.domain.path;

import java.util.List;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.fare.farestrategy.charge.DistanceStandard;
import wooteco.subway.domain.fare.farestrategy.discount.AgeStandard;
import wooteco.subway.exception.ExceptionMessage;
import wooteco.subway.exception.NotFoundException;

public class Path {

    private final List<Station> stations;
    private final long extraFare;
    private final int distance;

    public Path(List<Station> stations, long extraFare, int distance) {
        this.stations = stations;
        this.extraFare = extraFare;
        this.distance = distance;
    }

    public static Path of(List<Station> stations, List<Long> extraFares, int distance) {
        return new Path(stations, findMaxExtraFare(extraFares), distance);
    }

    private static long findMaxExtraFare(List<Long> extraFares) {
        return extraFares.stream().mapToLong(x -> x)
                .max()
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.NOT_FOUND_EXTRA_FARE.getContent()));
    }

    public Fare calculateFare(int age) {
        return new Fare(1250L)
                .chargeOf(DistanceStandard.findStrategy(distance))
                .chargeOf(x -> x + extraFare)
                .discountOf(AgeStandard.findStrategy(age));
    }

    public List<Station> getStations() {
        return stations;
    }

    public long getExtraFare() {
        return extraFare;
    }

    public int getDistance() {
        return distance;
    }
}
