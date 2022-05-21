package wooteco.subway.domain.section;

import wooteco.subway.domain.section.creationStrategy.CreationStrategy;
import wooteco.subway.domain.section.deletionStrategy.DeletionStrategy;
import wooteco.subway.domain.section.sortStrategy.SortStrategy;
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

    public Optional<Section> getSectionOverLappedBy(Section section) {
        return creationStrategy.getSectionOverLappedBy(sections, section);
    }

    public Section fixOverLappedSection(Section overLappedSection, Section section) {
        return creationStrategy.fixOverLappedSection(sections, overLappedSection, section);
    }

    public void delete(Long lineId, Station station) {
        deletionStrategy.delete(sections, lineId, station);
    }

    public Optional<Section> fixDisconnectedSection(Long lineId, Station station) {
        return deletionStrategy.fixDisconnectedSection(sections, lineId, station);
    }

    public List<Station> getSortedStations() {
        return sortStrategy.sort(sections);
    }

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Section section) {
        sections.add(section);
    }
}
