package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class Sections {

    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public List<Long> getStationIds() {
        return sections.stream()
                .map(Section::getAllStations)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public Section findSection(final Long upStationId, final Long downStationId) {
        return sections.stream()
                .filter(section -> section.hasStationId(upStationId) && section.hasStationId(downStationId))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("맞는 섹션 없음"));
    }

    public List<Section> getSections() {
        return new ArrayList<>(sections);
    }
}
