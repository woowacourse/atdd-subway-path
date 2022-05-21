package wooteco.subway.domain;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Sections {

    private static final int MIN_STATIONS_COUNT = 2;
    private static final String NO_DOWN_STATION_ID_ERROR = "해당 stationId를 하행역으로 둔 구간은 존재하지 않습니다.";
    private static final String NO_STATIONS_IN_LINE_ERROR = "해당 역은 기존 노선과 이어지지 않습니다.";
    private static final String DUPLICATED_SECTION_LIST_ERROR = "해당 구간은 이미 등록되어 있습니다.";
    private static final String CREATE_CROSSROADS_LIST_ERROR = "갈림길을 생성할 수 없습니다.";
    private static final String MIN_STATIONS_COUNT_ERROR = String.format("노선은 최소 %d개의 역을 갖고 있어야 합니다.",
            MIN_STATIONS_COUNT);

    private final List<Section> value;

    public Sections(List<Section> value) {
        this.value = value;
    }

    public List<Long> getSortedStationIds() {
        List<Long> ids = new LinkedList<>();
        addUpStationIds(ids, value.get(0).getDownStationId());
        addDownStationIds(ids, value.get(0).getUpStationId());

        return ids;
    }

    private void addUpStationIds(List<Long> ids, Long nowId) {
        Map<Long, Long> sectionMap = initMapByUpStationKey();
        while (nowId != null) {
            ids.add(nowId);
            nowId = sectionMap.get(nowId);
        }
    }

    private Map<Long, Long> initMapByUpStationKey() {
        return value.stream()
                .collect(Collectors.toMap(Section::getUpStationId, Section::getDownStationId));
    }

    private void addDownStationIds(List<Long> ids, Long nowId) {
        Map<Long, Long> sectionMap = initMapByDownStationKey();
        while (nowId != null) {
            ids.add(0, nowId);
            nowId = sectionMap.get(nowId);
        }
    }

    private Map<Long, Long> initMapByDownStationKey() {
        return value.stream()
                .collect(Collectors.toMap(Section::getDownStationId, Section::getUpStationId));
    }

    /**
     * 새로운 구간을 등록하는 메서드
     *
     * @param newSection 추가하고자 하는 구간
     * @return 데이터가 변경된 Section
     */
    public Optional<Section> findUpdateWhenAdd(Section newSection) {
        validNewSection(newSection);

        for (Section section : value) {
            if (newSection.isSameDownStationId(section)) {
                throw new IllegalArgumentException(CREATE_CROSSROADS_LIST_ERROR);
            }

            if (newSection.isSameUpStationId(section)) {
                section.updateUpStationId(newSection.getDownStationId());
                section.reduceDistance(newSection);
                return Optional.of(section);
            }
        }
        return Optional.empty();
    }

    private void validNewSection(Section section) {
        Set<Long> allSectionIds = findStationIds();
        Long downStationId = section.getDownStationId();
        Long upStationId = section.getUpStationId();

        if (!allSectionIds.contains(downStationId) && !allSectionIds.contains(upStationId)) {
            throw new IllegalArgumentException(NO_STATIONS_IN_LINE_ERROR);
        }

        if (allSectionIds.containsAll(List.of(downStationId, upStationId))) {
            throw new IllegalArgumentException(DUPLICATED_SECTION_LIST_ERROR);
        }
    }

    private Set<Long> findStationIds() {
        Set<Long> ids = new HashSet<>();
        for (Section section : value) {
            ids.add(section.getUpStationId());
            ids.add(section.getDownStationId());
        }
        return ids;
    }

    /**
     * 기존 구간을 삭제하는 메서드
     *
     * @param stationId 삭제하고자 하는 역
     * @return 삭제로 인해 변경 사항이 있는 Section
     */
    public Optional<Section> findUpdateWhenRemove(Long stationId) {
        validRemoveCondition();
        Section removedSection = findByDownStationId(stationId);

        for (Section section : value) {
            if (section.isSameUpStationId(stationId)) {
                section.updateUpStationId(removedSection.getUpStationId());
                section.addDistance(removedSection);
                return Optional.of(section);
            }
        }
        return Optional.empty();
    }

    private void validRemoveCondition() {
        if (value.size() <= MIN_STATIONS_COUNT) {
            throw new IllegalArgumentException(MIN_STATIONS_COUNT_ERROR);
        }
    }

    public Section findByDownStationId(Long stationId) {
        return value.stream()
                .filter(section -> section.isSameDownStationId(stationId))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(NO_DOWN_STATION_ID_ERROR));
    }

    @Override
    public String toString() {
        return "Sections{" +
                "value=" + value +
                '}';
    }
}
