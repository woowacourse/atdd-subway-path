package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.exception.NotFoundSectionException;

public final class Sections {

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Long> getStationIds() {
        return sections.stream()
                .map(Section::getAllStations)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public Section findSection(Long upStationId, Long downStationId) {
        return sections.stream()
                .filter(section -> section.hasStationId(upStationId) && section.hasStationId(downStationId))
                .findAny()
                .orElseThrow(NotFoundSectionException::new);
    }

    public List<Section> getSections() {
        return new ArrayList<>(sections);
    }
}
