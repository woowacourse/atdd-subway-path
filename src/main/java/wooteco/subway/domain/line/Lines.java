package wooteco.subway.domain.line;

import java.util.List;
import wooteco.subway.exception.datanotfound.LineNotFoundException;

public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public int findMaxExtraFare(final List<Long> lineIds) {
        return lineIds.stream()
                .mapToInt(id -> findExtraFare(id).getExtraFare())
                .max()
                .orElseThrow(() -> new LineNotFoundException("Line이 존재하지 않습니다."));
    }

    private Line findExtraFare(final Long id) {
        return lines.stream()
                .filter(line -> line.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new LineNotFoundException("Line이 존재하지 않습니다."));
    }
}
