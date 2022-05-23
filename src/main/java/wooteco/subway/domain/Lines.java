package wooteco.subway.domain;

import java.util.List;

public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public Line getLineByLineId(long lineId) {
        return lines.stream()
                .filter(line -> line.isSameId(lineId))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));
    }
}
