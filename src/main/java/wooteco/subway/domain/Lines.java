package wooteco.subway.domain;

import java.util.List;

public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public int getMaxExtraFare() {
        return lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElseThrow();
    }
}
