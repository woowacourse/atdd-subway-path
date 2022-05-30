package wooteco.subway.domain;

import java.util.List;
import java.util.NoSuchElementException;

public class Lines {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public Line find(Long id) {
        return lines.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 노선 아이디입니다."));
    }
}
