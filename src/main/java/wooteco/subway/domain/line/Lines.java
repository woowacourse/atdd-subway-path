package wooteco.subway.domain.line;

import java.util.List;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.line.Line;

public class Lines {

    private static final int DEFAULT_EXTRA_LINE_FARE = 0;

    private final List<Line> values;

    public Lines(List<Line> values) {
        this.values = values;
    }

    public int calculateExtraLineFare(final List<Station> stations) {
        return values.stream()
                .filter(line -> doesLineContainStation(line, stations))
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(DEFAULT_EXTRA_LINE_FARE);
    }

    private boolean doesLineContainStation(final Line line, final List<Station> stations) {
        return stations.stream().anyMatch(line::containStation);
    }
}
