package wooteco.subway.domain;

import java.util.Collections;
import java.util.List;

public class Path {

    private static final int BASIC_FARE = 1250;
    private static final int FIRST_SECTION_FULL_FARE = 800;
    private static final int FIRST_SECTION_UNIT = 5;
    private static final int SECOND_SECTION_UNIT = 8;

    private final List<Long> stationIds;
    private final int distance;

    public Path(List<Long> stationIds, int distance) {
        this.stationIds = stationIds;
        this.distance = distance;
    }

    public int calculateFare() {
        if (distance < 10) {
            return BASIC_FARE;
        }
        if (distance <= 50) {
            return calcAdditionalFare(distance - 10, FIRST_SECTION_UNIT);
        }
        return FIRST_SECTION_FULL_FARE + calcAdditionalFare(distance - 50, SECOND_SECTION_UNIT);
    }

    private int calcAdditionalFare(int distance, int unit) {
        int fare = 0;
        fare += (distance / unit) * 100;
        if (distance % unit > 0 || (distance / unit == 0)) {
            fare += 100;
        }
        return BASIC_FARE + fare;
    }

    public List<Long> getStationIds() {
        return Collections.unmodifiableList(stationIds);
    }

    public int getDistance() {
        return distance;
    }
}
