package wooteco.subway.line.event;

import wooteco.subway.line.domain.Section;

import java.util.List;

public class LinesEvent {
    private List<Section> lines;

    public LinesEvent(List<Section> lines) {
        this.lines = lines;
    }

    public List<Section> getLines() {
        return lines;
    }
}
