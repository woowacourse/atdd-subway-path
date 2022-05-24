package wooteco.subway.domain.line;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;

public class Lines {

    private static final String LINES_AND_SECTIONS_NOT_MATCHING_EXCEPTION = "노선 정보와 구간들의 정보가 서로 불일치합니다.";

    private final List<LineMap> value;

    private Lines(List<LineMap> value) {
        this.value = value;
    }

    public static Lines of(List<Line> lines, List<Section> sections) {
        validateRegisteredLines(lines, sections);
        List<LineMap> lineMaps = lines.stream()
                .map(it -> toLine(it, sections))
                .sorted(Comparator.comparingLong(LineMap::getId))
                .collect(Collectors.toList());
        return new Lines(lineMaps);
    }

    private static void validateRegisteredLines(List<Line> lines, List<Section> sections) {
        if (!extractAllLineIds(lines).containsAll(extractAllRegisteredLineIds(sections))) {
            throw new RuntimeException(LINES_AND_SECTIONS_NOT_MATCHING_EXCEPTION);
        }
    }

    private static List<Long> extractAllLineIds(List<Line> lines) {
        return lines.stream()
                .map(Line::getId)
                .collect(Collectors.toList());
    }

    private static List<Long> extractAllRegisteredLineIds(List<Section> sections) {
        return sections.stream()
                .map(Section::getLineId)
                .distinct()
                .collect(Collectors.toList());
    }

    private static LineMap toLine(Line line, List<Section> sections) {
        Long lineId = line.getId();
        List<Section> registeredSections = extractRegisteredSections(sections, lineId);
        return new LineMap(line, new Sections(registeredSections));
    }

    private static List<Section> extractRegisteredSections(List<Section> sections, Long lineId) {
        return sections.stream()
                .filter(it -> it.isRegisteredAtLine(lineId))
                .collect(Collectors.toList());
    }

    public List<LineMap> toSortedList() {
        return value;
    }
}
