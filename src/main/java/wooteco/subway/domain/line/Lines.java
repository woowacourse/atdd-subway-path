package wooteco.subway.domain.line;

import java.util.ArrayList;
import java.util.List;

public class Lines {

    private static final int DEFAULT_EXTRA_FARE = 0;

    private final List<Line> value;

    public Lines(List<Line> value) {
        this.value = new ArrayList<>(value);
    }

    public int getMaxExtraFare() {
        return value.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(DEFAULT_EXTRA_FARE);
    }
}
