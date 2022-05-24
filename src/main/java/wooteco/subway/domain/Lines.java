package wooteco.subway.domain;

import java.util.List;

public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public int findMaxExtraFare() {
        return lines.stream()
                .mapToInt(line -> line.getExtraFare())
                .max()
                .orElse(0);
    }
}
