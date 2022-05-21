package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Sections {

    private static final int MINIMUM_SIZE = 1;

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public boolean isMiddleSection(Section section) {
        long upStationId = findUpTerminalStationId();
        long downStationId = findDownTerminalStationId();
        return isMiddlePoint(section, upStationId, downStationId);
    }

    private boolean isMiddlePoint(Section section, long upStationId, long downStationId) {
        return !(section.matchDownStationId(upStationId) || section.mathUpStationId(downStationId));
    }

    public boolean hasStationId(long id) {
        Set<Long> stationIds = new HashSet<>();
        for (Section section : sections) {
            stationIds.add(section.getUpStationId());
            stationIds.add(section.getDownStationId());
        }
        return stationIds.contains(id);
    }

    public Section findSectionByUpStationId(long id) {
        return sections.stream()
            .filter(i -> i.mathUpStationId(id))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("구간 중 해당 upStationId이 존재하지 않습니다."));
    }

    public Section findSectionByDownStationId(long id) {
        return sections.stream()
            .filter(i -> i.matchDownStationId(id))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("구간 중 해당 downStationId 존재하지 않습니다."));
    }

    private Map<Long, Long> getSectionIds() {
        Map<Long, Long> sectionIds = new HashMap<>();
        for (Section section : sections) {
            sectionIds.put(section.getUpStationId(), section.getDownStationId());
        }
        return sectionIds;
    }

    private long findDownTerminalStationId() {
        Map<Long, Long> sectionIds = getSectionIds();
        return sectionIds.values().stream()
            .filter(i -> !sectionIds.containsKey(i))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("하행점을 찾을 수 없습니다."));
    }

    private long findUpTerminalStationId() {
        Map<Long, Long> sectionIds = getSectionIds();
        return sectionIds.keySet().stream()
            .filter(i -> !sectionIds.containsValue(i))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("상행점을 찾을 수 없습니다."));
    }

    public List<Long> sortedStationId() {
        long upStationId = findUpTerminalStationId();
        List<Long> stationIds = new ArrayList<>(List.of(upStationId));
        Map<Long, Long> stationIdsInSections = getSectionIds();

        for (int i = 0; i < stationIdsInSections.size(); i++) {
            upStationId = stationIdsInSections.get(upStationId);
            stationIds.add(upStationId);
        }

        return stationIds;
    }

    private boolean isSingleSection() {
        return sections.size() == MINIMUM_SIZE;
    }

    public boolean canRemoveSection() {
        return !(isSingleSection() || sections.isEmpty());
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }
}
