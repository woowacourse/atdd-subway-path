package wooteco.subway.domain.fare;

import java.util.Comparator;
import java.util.List;
import wooteco.subway.domain.line.Line;

public class LineOverFare extends Decorator {

    private static final String LINE_INFO_NOT_FOUND_EXCEPTION = "노선 정보가 제공되지 않았습니다.";
    private final List<Line> lines;

    public LineOverFare(Fare delegate, List<Line> lines) {
        super(delegate);
        this.lines = lines;
    }

    @Override
    public int calculate() {
        int fare = super.delegate();
        int lineExtraFare = calculateMaxExtraFare();
        return fare + lineExtraFare;
    }

    private Integer calculateMaxExtraFare() {
        return lines.stream()
                .map(Line::getExtraFare)
                .max(Comparator.comparingInt(Integer::intValue))
                .orElseThrow(() -> new IllegalArgumentException(LINE_INFO_NOT_FOUND_EXCEPTION));
    }
}
