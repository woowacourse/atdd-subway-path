package wooteco.subway.domain;

import java.util.List;

public class Lines {

    private final List<Line> values;

    public Lines(final List<Line> values) {
        this.values = values;
    }

    public List<Line> getValues() {
        return values;
    }

    public int getHighestOverFare() {
        return values.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
    }
}
