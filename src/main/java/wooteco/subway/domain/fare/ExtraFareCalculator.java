package wooteco.subway.domain.fare;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.section.Section;

public class ExtraFareCalculator {
    private final List<Section> sections;
    private final List<Line> lines;

    public ExtraFareCalculator(List<Section> sections, List<Line> lines) {
        this.sections = sections;
        this.lines = lines;
    }

    public int getMostExpensiveExtraFare(List<Long> path) {
        List<Long> lineIds = sections.stream()
                .filter(it -> path.contains(it.getUpStationId())
                        || path.contains(it. getDownStationId()))
                .map(Section::getLineId)
                .collect(Collectors.toList());

        return lines.stream()
                .filter(it -> lineIds.contains(it.getId()))
                .distinct()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElseThrow(NoSuchElementException::new);
    }
}
