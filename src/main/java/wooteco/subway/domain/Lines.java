package wooteco.subway.domain;

import java.util.List;

public class Lines {

    public static final int STANDARD = 0;

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public int findMaxExtraFare() {
        return lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(STANDARD);
    }
}
