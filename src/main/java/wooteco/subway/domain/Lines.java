package wooteco.subway.domain;

import java.util.List;
import wooteco.subway.exception.SubwayException;

public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public void validateDuplication(Line line) {
        validateNameForSave(line);
        validateColorForSave(line);
    }

    private void validateNameForSave(Line line) {
        if (containsName(line)) {
            throw new SubwayException("지하철 노선 이름이 중복됩니다.");
        }
    }

    private void validateColorForSave(Line line) {
        if (containsColor(line)) {
            throw new SubwayException("지하철 노선 색상이 중복됩니다.");
        }
    }

    private boolean containsName(Line line) {
        return lines.stream()
                .filter(it -> !it.isSameId(line))
                .anyMatch(it -> it.isSameName(line));
    }

    private boolean containsColor(Line line) {
        return lines.stream()
                .filter(it -> !it.isSameId(line))
                .anyMatch(it -> it.isSameColor(line));
    }

    public List<Line> getLines() {
        return lines;
    }
}
