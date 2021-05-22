package wooteco.subway.line.domain.section;

import wooteco.subway.station.domain.Station;

import java.util.*;
import java.util.stream.Collectors;

public class Sections {

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections empty() {
        return new Sections(new ArrayList<>());
    }

    public void addSection(Section section) {
        if (this.sections.isEmpty()) {
            this.sections.add(section);
            return;
        }

        checkAlreadyExisted(section);
        checkExistedAny(section);

        addSectionUpToUp(section);
        addSectionDownToDown(section);

        this.sections.add(section);
    }

    private void checkAlreadyExisted(Section section) {
        List<Station> stations = getStations();

        if (!stations.contains(section.getUpStation()) &&
                !stations.contains(section.getDownStation())) {
            throw new RuntimeException();
        }
    }

    private void checkExistedAny(Section section) {
        List<Station> stations = getStations();
        List<Station> stationsOfNewSection = Arrays.asList(
                section.getUpStation(),
                section.getDownStation()
        );

        if (stations.containsAll(stationsOfNewSection)) {
            throw new RuntimeException();
        }
    }

    private void addSectionUpToUp(Section section) {
        this.sections.stream()
                .filter(it -> it.hasSameUpStation(section))
                .findFirst()
                .ifPresent(it -> replaceSectionWithDownStation(section, it));
    }

    private void replaceSectionWithDownStation(Section newSection, Section existSection) {
        if (existSection.compareDistance(newSection) < 0) {
            throw new RuntimeException();
        }

        sections.remove(existSection);
        sections.add(
                new Section(
                        newSection.getDownStation(),
                        existSection.getDownStation(),
                        existSection.getDistance() - newSection.getDistance()
                )
        );
    }

    private void addSectionDownToDown(Section section) {
        this.sections.stream()
                .filter(it -> it.getDownStation().equals(section.getDownStation()))
                .findFirst()
                .ifPresent(it -> replaceSectionWithUpStation(section, it));
    }

    private void replaceSectionWithUpStation(Section newSection, Section existSection) {
        if (existSection.getDistance() <= newSection.getDistance()) {
            throw new RuntimeException();
        }

        this.sections.remove(existSection);
        this.sections.add(
                new Section(
                        existSection.getUpStation(),
                        newSection.getUpStation(),
                        existSection.getDistance() - newSection.getDistance()
                )
        );
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        return createStations();
    }

    private List<Station> createStations() {
        List<Station> stations = new ArrayList<>();
        Section firstSection = findFirstSection();
        stations.add(firstSection.getUpStation());

        Section nextSection = firstSection;

        while (nextSection != null) {
            stations.add(nextSection.getDownStation());
            nextSection = findSectionByNextUpStation(nextSection.getDownStation());
        }

        return stations;
    }

    private Section findFirstSection() {
        List<Station> downStations = this.sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());

        return this.sections.stream()
                .filter(it -> !downStations.contains(it.getUpStation()))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private Section findSectionByNextUpStation(Station station) {
        return this.sections.stream()
                .filter(it -> it.isSameUpStation(station))
                .findFirst()
                .orElse(null);
    }

    public void removeStation(Station station) {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upSection = findUpSection(station);
        Optional<Section> downSection = findDownSection(station);

        if (upSection.isPresent() && downSection.isPresent()) {
            createNewSection(upSection.get(), downSection.get());
        }

        upSection.ifPresent(sections::remove);
        downSection.ifPresent(sections::remove);
    }

    private void createNewSection(Section upSection, Section downSection) {
        Station newUpStation = downSection.getUpStation();
        Station newDownStation = upSection.getDownStation();

        int newDistance = upSection.getDistance() + downSection.getDistance();

        sections.add(new Section(newUpStation, newDownStation, newDistance));
    }

    private Optional<Section> findDownSection(Station station) {
        return sections.stream()
                .filter(it -> it.isSameDownStation(station))
                .findFirst();
    }

    private Optional<Section> findUpSection(Station station) {
        return sections.stream()
                .filter(it -> it.isSameUpStation(station))
                .findFirst();
    }

    public List<Section> getSections() {
        return sections;
    }

}
