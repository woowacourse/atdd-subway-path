package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sections {

    private static final int MINIMUM_SIZE = 1;

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public boolean isMiddleSection(Section section) {
        Long upStationId = findUpTerminalStationId();
        Long downStationId = findDownTerminalStationId();
        return isMiddlePoint(section, upStationId, downStationId);
    }

    private boolean isMiddlePoint(Section section, Long upStationId, Long downStationId) {
        return !(section.matchDownStationId(upStationId) || section.mathUpStationId(downStationId));
    }

    public boolean hasStationId(Long id) {
        return sections.stream()
            .anyMatch(section -> section.matchDownStationId(id) || section.mathUpStationId(id));
    }

    public List<Section> insert(Section section) {
        if (isEndPointSection(section)) {
            sections.add(section);
            return sections;
        }
        return insertMiddleSection(section);
    }

    private boolean isEndPointSection(Section section) {
        Long upTerminalStationId = findUpTerminalStationId();
        Long downTerminalStationId = findDownTerminalStationId();
        return (section.matchDownStationId(upTerminalStationId) || section.mathUpStationId(
            downTerminalStationId));
    }

    private List<Section> insertMiddleSection(Section section) {
        if (hasStationId(section.getUpStationId())) {
            return updateDownStationSection(section);
        }
        return updateUpStationSection(section);
    }

    private List<Section> updateDownStationSection(Section section) {
        Section sectionByDownStation = findSectionByUpStationId(section.getUpStationId());
        validateUpdateDistance(sectionByDownStation, section);
        int distance = sectionByDownStation.getDistance() - section.getDistance();
        Section updateSection = new Section(sectionByDownStation.getLine(),
            section.getDownStationId(), sectionByDownStation.getDownStationId(), distance);
        sections.remove(sectionByDownStation);
        sections.add(section);
        sections.add(updateSection);
        return sections;
    }

    private List<Section> updateUpStationSection(Section section) {
        // 1-2-3 > 1-2-4-3 -> 2-3제거 , 2-4추가 4-3추가
        Section sectionByUpStation = findSectionByDownStationId(section.getDownStationId());
        int distance = sectionByUpStation.getDistance() - section.getDistance();
        Section updateSection = new Section(sectionByUpStation.getLine(),
            sectionByUpStation.getUpStationId(),
            section.getUpStationId(), distance);
        sections.remove(sectionByUpStation);
        sections.add(section);
        sections.add(updateSection);
        return sections;
    }

    private void validateUpdateDistance(Section section, Section insertSection) {
        if (section.getDistance() <= insertSection.getDistance()) {
            throw new IllegalArgumentException("등록할 구간의 길이가 기존 역 사이의 길이보다 길거나 같으면 안됩니다.");
        }
    }

    public Section findSectionByUpStationId(Long id) {
        return sections.stream()
            .filter(i -> i.mathUpStationId(id))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("구간 중 해당 upStationId이 존재하지 않습니다."));
    }

    public Section findSectionByDownStationId(Long id) {
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

    private Long findDownTerminalStationId() {
        Map<Long, Long> sectionIds = getSectionIds();
        return sectionIds.values().stream()
            .filter(i -> !sectionIds.containsKey(i))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("하행점을 찾을 수 없습니다."));
    }

    private Long findUpTerminalStationId() {
        Map<Long, Long> sectionIds = getSectionIds();
        return sectionIds.keySet().stream()
            .filter(i -> !sectionIds.containsValue(i))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("상행점을 찾을 수 없습니다."));
    }

    public List<Long> sortedStationId() {
        Long upStationId = findUpTerminalStationId();
        List<Long> stationIds = new ArrayList<>(List.of(upStationId));
        Map<Long, Long> stationIdsInSections = getSectionIds();

        for (int i = 0; i < stationIdsInSections.size(); i++) {
            upStationId = stationIdsInSections.get(upStationId);
            stationIds.add(upStationId);
        }

        return stationIds;
    }

    public List<Section> removeSection(Section section) {
        validateRemove();

        if (isEndPointSection(section)) {
            Section deleteSection = findSectionByUpStationId(section.getUpStationId());
            sections.remove(deleteSection);
            return sections;
        }

        return removeMiddleSection(section);
    }

    private List<Section> removeMiddleSection(Section section) {
        Section downSection = findSectionByUpStationId(section.getUpStationId());
        Section upSection = findSectionByDownStationId(section.getUpStationId());
        sections.remove(downSection);
        sections.remove(upSection);

        int distance = upSection.getDistance() + downSection.getDistance();
        Section updateSection = new Section(upSection.getLine(), upSection.getUpStationId(),
            downSection.getDownStationId(), distance);
        sections.add(updateSection);
        return sections;
    }

    private void validateRemove() {
        if (!canRemoveSection()) {
            throw new IllegalArgumentException("구간을 제거할 수 없는 상태입니다.");
        }
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
