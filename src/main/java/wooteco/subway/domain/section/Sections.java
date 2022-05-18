package wooteco.subway.domain.section;

import wooteco.subway.domain.station.Station;

import java.util.*;

public class Sections {
    private final List<Section> sections;
    private final CreationStrategy creationStrategy;
    private final DeletionStrategy deletionStrategy;
    private final SortStrategy sortStrategy;

    public Sections(List<Section> sections, CreationStrategy creationStrategy, DeletionStrategy deletionStrategy, SortStrategy sortStrategy) {
        this.sections = sections;
        this.creationStrategy = creationStrategy;
        this.deletionStrategy = deletionStrategy;
        this.sortStrategy = sortStrategy;
    }

    public void save(Section section) {
        creationStrategy.save(sections, section);
    }

    public Optional<Section> fixOverLappedSection(Section section) {
        return creationStrategy.fixOverLappedSection(sections, section);
    }

    public void delete(Long lineId, Long stationId) {
        deletionStrategy.delete(sections, lineId, stationId);
    }

    public Optional<Section> fixDisconnectedSection(Long lineId, Long stationId) {
        return deletionStrategy.fixDisconnectedSection(sections, lineId, stationId);
    }

    public List<Station> sort(List<Station> stations) {
        return sortStrategy.sort(sections, stations);
    }

    public List<Long> getStationIds() {
        Set<Long> stationIds = new HashSet<>();
        for (Section section : sections) {
            stationIds.add(section.getUpStationId());
            stationIds.add(section.getDownStationId());
        }

        return new ArrayList<>(stationIds);
    }

    public List<Section> getSections() {
        return sections;
    }
}
