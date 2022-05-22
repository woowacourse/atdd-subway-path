package wooteco.subway.domain;

import java.util.List;

public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public Line maxExtraFare() {
        Line maxLine = null;
        for (final Line line : lines) {
            maxLine = line.comparesMoreExpensiveExtraFare(maxLine);
        }
        return maxLine;
    }
}
