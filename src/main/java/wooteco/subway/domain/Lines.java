package wooteco.subway.domain;

import java.util.HashSet;
import java.util.Set;

public class Lines {

    private final Set<Line> lines;

    public Lines(final Set<Line> lines) {
        this.lines = new HashSet<>(lines);
    }

    public int mostExpensiveLineFare() {
        return lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElseThrow(() -> new IllegalStateException("최대 추가요금을 찾을 수 없습니다."));
    }
}
