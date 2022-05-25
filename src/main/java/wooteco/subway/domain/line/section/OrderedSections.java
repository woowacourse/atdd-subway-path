package wooteco.subway.domain.line.section;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderedSections {

    private static final int FIRST_INDEX = 0;
    private static final int ALLOWED_MINIMUM_SECTION_COUNT = 1;

    private final List<Section> orderedSections;

    public OrderedSections(List<Section> sections, SectionSorter sectionSorter) {
        List<Section> copiedSections = new ArrayList<>(sections);
        validateSectionsNotEmpty(copiedSections);
        this.orderedSections = sectionSorter.sort(copiedSections);
    }

    private static void validateSectionsNotEmpty(List<Section> sections) {
        if (sections.isEmpty()) {
            throw new IllegalArgumentException("지하철구간은 하나 이상이어야 합니다.");
        }
    }

    public void append(Section section) {
        validateSectionIsAppendable(section);

        if (canBeFirstSection(section)) {
            orderedSections.add(FIRST_INDEX, section);
            return;
        }

        if (canBeLastSection(section)) {
            orderedSections.add(section);
            return;
        }

        appendInMiddleIfPossible(section);
    }

    private void validateSectionIsAppendable(Section section) {
        boolean existenceOfUpStation = isUpStationAlreadyExist(section);
        boolean existenceOfDownStation = isDownStationAlreadyExist(section);

        validateStationsBothExist(existenceOfUpStation, existenceOfDownStation);
        validateStationsNeitherExist(existenceOfUpStation, existenceOfDownStation);
    }

    private boolean isUpStationAlreadyExist(Section target) {
        return orderedSections.stream()
                .anyMatch(section -> section.containsUpStationOf(target));
    }

    private boolean isDownStationAlreadyExist(Section target) {
        return orderedSections.stream()
                .anyMatch(section -> section.containsDownStationOf(target));
    }

    private void validateStationsBothExist(boolean existenceOfUpStation, boolean existenceOfDownStation) {
        if (existenceOfUpStation && existenceOfDownStation) {
            throw new IllegalArgumentException("해당 구간의 상행역과 하행역이 이미 노선에 존재합니다.");
        }
    }

    private void validateStationsNeitherExist(boolean existenceOfUpStation, boolean existenceOfDownStation) {
        if (!existenceOfUpStation && !existenceOfDownStation) {
            throw new IllegalArgumentException("해당 구간의 상행역과 하행역이 노선에 존재하지 않습니다.");
        }
    }

    private boolean canBeFirstSection(Section section) {
        Section firstSection = orderedSections.get(FIRST_INDEX);
        return section.isPreviousOf(firstSection);
    }

    private boolean canBeLastSection(Section section) {
        int lastIndex = orderedSections.size() - 1;
        Section lastSection = orderedSections.get(lastIndex);
        return lastSection.isPreviousOf(section);
    }

    private void appendInMiddleIfPossible(Section section) {
        findConnectableSection(section).ifPresent(middleSection -> {
            int middleIndex = orderedSections.indexOf(middleSection);
            List<Section> separatedSections = middleSection.split(section);

            orderedSections.remove(middleIndex);
            new LinkedList<>(separatedSections)
                    .descendingIterator()
                    .forEachRemaining(it -> orderedSections.add(middleIndex, it));
        });
    }

    private Optional<Section> findConnectableSection(Section section) {
        return orderedSections.stream()
                .filter(it -> section.equalsUpStation(it) || section.equalsDownStation(it))
                .findAny();
    }

    public void remove(long stationId) {
        validateStationContained(stationId);
        validateSectionsSizeEnough();

        if (isFirstStation(stationId)) {
            orderedSections.remove(FIRST_INDEX);
            return;
        }
        if (isLastStation(stationId)) {
            int lastIndex = orderedSections.size() - 1;
            orderedSections.remove(lastIndex);
            return;
        }

        removeMiddleStationIfPossible(stationId);
    }

    private void validateStationContained(long stationId) {
        if (!getStations().contains(stationId)) {
            throw new IllegalArgumentException("노선에 포함되어 있는 역이 아닙니다.");
        }
    }

    private void validateSectionsSizeEnough() {
        if (orderedSections.size() == ALLOWED_MINIMUM_SECTION_COUNT) {
            throw new IllegalStateException("노선의 구간이 하나이므로 구간을 삭제할 수 없습니다.");
        }
    }

    private boolean isFirstStation(long stationId) {
        Section firstSection = orderedSections.get(FIRST_INDEX);
        return firstSection.containsAsUpStation(stationId);
    }

    private boolean isLastStation(long stationId) {
        int lastIndex = orderedSections.size() - 1;
        Section lastSection = orderedSections.get(lastIndex);
        return lastSection.containsAsDownStation(stationId);
    }

    private void removeMiddleStationIfPossible(long stationId) {
        Section upSection = findSectionContainedAsUpStation(stationId);
        Section downSection = findSectionContainedAsDownStation(stationId);
        Section mergedSection = upSection.merge(downSection);

        int index = orderedSections.indexOf(upSection);
        orderedSections.remove(upSection);
        orderedSections.remove(downSection);
        orderedSections.add(index, mergedSection);
    }

    private Section findSectionContainedAsUpStation(long stationId) {
        return orderedSections.stream()
                .filter(section -> section.containsAsDownStation(stationId))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("연결 가능한 구간을 찾을 수 없습니다."));
    }

    private Section findSectionContainedAsDownStation(long stationId) {
        return orderedSections.stream()
                .filter(section -> section.containsAsUpStation(stationId))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("연결 가능한 구간을 찾을 수 없습니다."));
    }

    public List<Section> getSections() {
        return List.copyOf(orderedSections);
    }

    public List<Long> getStations() {
        Set<Long> stations = new LinkedHashSet<>();
        for (Section section : orderedSections) {
            stations.add(section.getUpStationId());
            stations.add(section.getDownStationId());
        }
        return stations.stream()
                .collect(Collectors.toUnmodifiableList());
    }
}
