package wooteco.subway.domain;

import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import wooteco.subway.exception.ExceptionMessage;
import wooteco.subway.exception.DomainException;
import wooteco.subway.exception.NotFoundException;

public class Sections {

    private static final int MINIMUM_SECTIONS_FOR_DELETE = 2;
    private static final int NO_UPPER_SECTION_EXISTS = 0;
    private static final int MERGEABLE_SECTION_COUNT = 2;

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        if (sections == null) {
            this.sections = new ArrayList<>();
            return;
        }
        this.sections = new ArrayList<>(sections);
    }

    public void add(Section section) {
        checkInsertSectionsStations(section);
        findDividableSection(section)
                .ifPresent(it -> update(it, it.divideFrom(section)));
        sections.add(section);
    }

    private Optional<Section> findDividableSection(Section section) {
        return sections.stream()
                .filter(it -> it.isForDivide(section))
                .findFirst();
    }

    private void update(Section from, Section to) {
        sections.remove(from);
        sections.add(to);
    }

    private void checkInsertSectionsStations(Section section) {
        if (!sections.isEmpty() && isAlreadyConnected(section)) {
            throw new DomainException(ExceptionMessage.INSERT_DUPLICATED_SECTION.getContent());
        }
        if (!sections.isEmpty() && unableConnect(section)) {
            throw new DomainException(ExceptionMessage.INSERT_SECTION_NOT_MATCH.getContent());
        }
    }

    private boolean unableConnect(Section section) {
        List<Station> stations = getSortedStation();
        return !stations.contains(section.getDownStation())
                && !stations.contains(section.getUpStation());
    }

    private boolean isAlreadyConnected(Section section) {
        List<Station> station = getSortedStation();
        return station.contains(section.getDownStation()) && station.contains(section.getUpStation());
    }

    public List<Station> getSortedStation() {
        Map<Station, Section> sectionWithUpStation = getSectionWithUpStation();
        Station firstStation = getFirstStation();
        List<Station> sorted = new ArrayList<>();
        sorted.add(firstStation);
        while (!sectionWithUpStation.isEmpty()) {
            Section section = sectionWithUpStation.get(firstStation);
            sorted.add(section.getDownStation());
            sectionWithUpStation.remove(firstStation);
            firstStation = section.getDownStation();
        }
        return sorted;
    }

    private Map<Station, Section> getSectionWithUpStation() {
        return sections.stream()
                .collect(toMap(Section::getUpStation, Function.identity()));
    }

    private List<Station> getDownStations() {
        return sections.stream().map(Section::getDownStation).collect(Collectors.toList());
    }

    private List<Station> getUpStations() {
        return sections.stream().map(Section::getUpStation).collect(Collectors.toList());
    }

    private Station getFirstStation() {
        List<Station> upStations = getUpStations();
        List<Station> downStations = getDownStations();
        return upStations.stream()
                .filter(upStation -> !downStations.contains(upStation))
                .reduce((one, another) -> {
                    throw new DomainException(ExceptionMessage.NOT_CONNECTED_SECTIONS.getContent());
                })
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.SECTIONS_ROTATE.getContent()));
    }

    public void deleteNearBy(Station station) {
        if (sections.size() < MINIMUM_SECTIONS_FOR_DELETE) {
            throw new DomainException(ExceptionMessage.SECTIONS_NOT_DELETABLE.getContent());
        }

        List<Section> nearSections = findNearSections(station);
        removeNearSections(nearSections);
        mergeSections(nearSections).ifPresent(sections::add);
    }

    private void removeNearSections(List<Section> nearSections) {
        for (Section section : nearSections) {
            sections.remove(section);
        }
    }

    private List<Section> findNearSections(Station station) {
        return sections.stream()
                .filter(it -> it.hasStation(station))
                .collect(Collectors.toList());
    }

    private Optional<Section> mergeSections(List<Section> nearSections) {
        if (nearSections.size() < MERGEABLE_SECTION_COUNT) {
            return Optional.empty();
        }
        Section from = nearSections.get(0);
        Section to = nearSections.get(1);
        return Optional.of(from.merge(to));
    }

    public List<Section> getValue() {
        return new ArrayList<>(sections);
    }
}
