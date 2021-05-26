package wooteco.subway.line.event;

import wooteco.subway.line.domain.Line;

import java.util.List;

public class LineUpdatedEvent {
    private List<Line> line;

    public LineUpdatedEvent(final List<Line> line) {
        this.line = line;
    }

    public List<Line> getLine() {
        return line;
    }
}
