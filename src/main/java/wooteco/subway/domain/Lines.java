package wooteco.subway.domain;

import java.util.Comparator;
import java.util.List;

public class Lines {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public int findMaxExtraFare() {
        return lines.stream()
            .map(Line::getExtraFare)
            .max(Comparator.naturalOrder())
            .get();
    }
}
