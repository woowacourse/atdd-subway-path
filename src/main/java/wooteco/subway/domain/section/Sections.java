package wooteco.subway.domain.section;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import wooteco.subway.domain.station.Station;

public class Sections {

    private static final int MIN_SECTION_COUNT = 1;
    private static final String NO_DOWN_STATION_ID_ERROR = "해당 stationId를 하행역으로 둔 구간은 존재하지 않습니다.\n -> stationId: %d";
    private static final String NO_UP_STATION_ID_ERROR = "해당 stationId를 상행역으로 둔 구간은 존재하지 않습니다.\n -> stationId: %d";
    private static final String NO_STATIONS_IN_LINE_ERROR = "해당 구간은 기존 노선과 이어지지 않습니다.\n -> %s";
    private static final String NO_STATION_IN_SECTION_ERROR = "해당 역은 구간에 포함되지 않습니다.\n -> stationId: %d";
    private static final String DUPLICATED_SECTION_LIST_ERROR = "해당 구간은 이미 등록되어 있습니다.\n -> upStationId: %d, downStationId: %d";
    private static final String CREATE_CROSSROADS_LIST_ERROR = "갈림길을 생성할 수 없습니다.";
    private static final String MIN_STATIONS_COUNT_ERROR = String.format("노선은 최소 %d개의 역을 갖고 있어야 합니다.",
            MIN_SECTION_COUNT);

    private final List<Section> value;

    public Sections(List<Section> value) {
        this.value = value;
    }

    public List<Long> getSortedStationIds() {
        List<Long> ids = new LinkedList<>();
        addUpStationIds(ids, value.get(0).getDownStation().getId());
        addDownStationIds(ids, value.get(0).getUpStation().getId());

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
                .collect(Collectors.toMap(section -> section.getUpStation().getId(),
                        section -> section.getDownStation().getId()
                        , (section1, section2) -> section1));
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
                .collect(Collectors.toMap(section -> section.getDownStation().getId(),
                        section -> section.getUpStation().getId()));
    }

    /**
     * 새로운 구간을 등록할 때 변경되는 기존 구간을 찾는 메서드. 종착역으로 등록되는 경우 변경되는 기존 구간이 없다.
     *
     * @param newSection 추가하고자 하는 구간
     * @return 데이터가 변경된 Section
     */
    public Optional<Section> findUpdateWhenAdd(Section newSection) {
        validNewSection(newSection);
        validCrossPath(newSection);

        Optional<Section> section = value.stream()
                .filter(newSection::isSameUpStationId)
                .findAny();

        section.ifPresent(it -> {
            it.updateUpStationId(newSection.getDownStation());
            it.reduceDistance(newSection);
        });

        return section;
    }

    private void validCrossPath(Section existSection) {
        boolean result = value.stream()
                .anyMatch(existSection::isSameDownStationId);
        if (result) {
            throw new IllegalArgumentException(CREATE_CROSSROADS_LIST_ERROR);
        }
    }

    private void validNewSection(Section section) {
        Set<Station> allSectionIds = findStations();
        Station downStation = section.getDownStation();
        Station upStation = section.getUpStation();

        if (!allSectionIds.contains(downStation) && !allSectionIds.contains(upStation)) {
            throw new IllegalArgumentException(String.format(NO_STATIONS_IN_LINE_ERROR, section));
        }

        if (allSectionIds.containsAll(List.of(downStation, upStation))) {
            throw new IllegalArgumentException(
                    String.format(DUPLICATED_SECTION_LIST_ERROR, upStation.getId(), downStation.getId()));
        }
    }

    private Set<Station> findStations() {
        Set<Station> stations = new HashSet<>();
        for (Section section : value) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }
        return stations;
    }

    /**
     * 역 id를 통해 삭제하는 구간의 id를 구하는 메서드.
     *
     * @param stationId 삭제하고자 하는 역
     * @return 삭제되는 구간의 id
     */
    public Long findRemoveSectionId(Long stationId) {
        validRemoveCondition();
        validStationId(stationId);

        try {
            return findByDownStationId(stationId).getId();
        } catch (IllegalArgumentException e) {
            return findByUpStationId(stationId).getId();
        }
    }

    /**
     * 기존 구간을 삭제할 때 변경되는 기존 구간을 찾는 메서드. 종착역이 삭제되는 경우 변경되는 기존 구간은 없다.
     *
     * @param stationId 삭제하고자 하는 역
     * @return 삭제로 인해 변경 사항이 있는 Section
     */
    public Optional<Section> findUpdateWhenRemove(Long stationId) {
        validRemoveCondition();
        validStationId(stationId);

        if (isLastStation(stationId)) {
            return Optional.empty();
        }

        Section removedSection = findByDownStationId(stationId);
        Section updatedSection = findByUpStationId(stationId);
        updatedSection.updateUpStationId(removedSection.getUpStation());
        updatedSection.addDistance(removedSection);

        return Optional.of(updatedSection);
    }

    private void validRemoveCondition() {
        if (value.size() <= MIN_SECTION_COUNT) {
            throw new IllegalArgumentException(MIN_STATIONS_COUNT_ERROR);
        }
    }

    private void validStationId(Long id) {
        boolean condition = value.stream()
                .noneMatch(section -> section.isSameDownStationId(id) || section.isSameUpStationId(id));

        if (condition) {
            throw new IllegalArgumentException(String.format(NO_STATION_IN_SECTION_ERROR, id));
        }
    }

    private boolean isLastStation(Long stationId) {
        List<Long> stationIds = getSortedStationIds();
        int index = stationIds.indexOf(stationId);
        return index == 0 || index == stationIds.size() - 1;
    }

    private Section findByDownStationId(Long stationId) {
        return value.stream()
                .filter(section -> section.isSameDownStationId(stationId))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format(NO_DOWN_STATION_ID_ERROR, stationId)));
    }

    private Section findByUpStationId(Long stationId) {
        return value.stream()
                .filter(section -> section.isSameUpStationId(stationId))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format(NO_UP_STATION_ID_ERROR, stationId)));
    }

    @Override
    public String toString() {
        return "Sections{" +
                "value=" + value +
                '}';
    }
}
