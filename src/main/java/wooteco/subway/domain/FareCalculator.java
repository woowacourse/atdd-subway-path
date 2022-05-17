package wooteco.subway.domain;

import java.util.List;

public class FareCalculator {

    private final List<Station> stations;

    public FareCalculator(List<Station> stations) {
        this.stations = stations;
    }

}
