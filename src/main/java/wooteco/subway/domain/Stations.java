package wooteco.subway.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Stations {

    private final List<Station> values;

    public Stations(final List<Station> values) {
        this.values = values;
    }

    public int calculateMatchCount(final Long... ids) {
        final List<Long> stationIds = values.stream()
                .map(Station::getId)
                .collect(Collectors.toList());

        return (int) Arrays.stream(ids)
                .filter(stationIds::contains)
                .count();
    }
}
