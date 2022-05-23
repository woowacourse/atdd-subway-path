package wooteco.subway.domain;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class Lines {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public int findMaxExtraFare() {
        return lines.stream()
            .map(Line::getExtraFare)
            .max(Comparator.naturalOrder())
            .orElseThrow(() -> new NoSuchElementException("노선 추가요금을 찾을 수 없습니다."));
    }
}
