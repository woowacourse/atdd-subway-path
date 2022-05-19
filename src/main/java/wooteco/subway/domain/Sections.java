package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.exception.NotFoundSectionException;

public class Sections {

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
                .orElseThrow(() -> new NotFoundSectionException("맞는 섹션 없음"));
    }

    public List<Section> getSections() {
        return new ArrayList<>(sections);
    }
}
