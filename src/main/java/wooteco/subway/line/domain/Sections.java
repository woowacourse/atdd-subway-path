package wooteco.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import wooteco.subway.exception.application.ValidationFailureException;
import wooteco.subway.station.domain.Station;

public class Sections {

    public static final int MINIMUM_STATIONS_IN_SECTIONS = 2;

    private final List<Section> sections;

    public Sections() {
        this(new ArrayList<>());
    }

    public Sections(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
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
        List<Station> stationsOfNewSection = Arrays.asList(section.getUpStation(), section.getDownStation());
        if (stations.containsAll(stationsOfNewSection)) {
            throw new ValidationFailureException("추가하려는 상행역과 하행역이 이미 구간내에 존재합니다.");
        }
    }

    private void checkExistedAny(Section section) {
        List<Station> stations = getStations();
        if (!stations.contains(section.getUpStation()) && !stations.contains(section.getDownStation())) {
            throw new ValidationFailureException("추가하려는 상행역과 하행역 중 하나는 구간 내에 있어야 합니다.");
        }
    }

    private void addSectionUpToUp(Section section) {
        this.sections.stream()
            .filter(existentSection -> existentSection.getUpStation().equals(section.getUpStation()))
            .findFirst()
            .ifPresent(existentSection -> replaceSectionWithDownStation(section, existentSection));
    }

    private void replaceSectionWithDownStation(Section newSection, Section existSection) {
        if (existSection.getDistance() <= newSection.getDistance()) {
            throw new ValidationFailureException("추가하려는 구간의 거리가 기존 구간의 거리보다 크거나 같습니다.");
        }
        this.sections.add(new Section(newSection.getDownStation(), existSection.getDownStation(),
            existSection.getDistance() - newSection.getDistance()));
        this.sections.remove(existSection);
    }

    private void addSectionDownToDown(Section section) {
        this.sections.stream()
            .filter(existentSection -> existentSection.getDownStation().equals(section.getDownStation()))
            .findFirst()
            .ifPresent(existentSection -> replaceSectionWithUpStation(section, existentSection));
    }

    private void replaceSectionWithUpStation(Section newSection, Section existentSection) {
        if (existentSection.getDistance() <= newSection.getDistance()) {
            throw new ValidationFailureException("추가하려는 구간의 거리가 기존 구간의 거리보다 크거나 같습니다.");
        }
        this.sections.add(new Section(existentSection.getUpStation(), newSection.getUpStation(),
            existentSection.getDistance() - newSection.getDistance()));
        this.sections.remove(existentSection);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Section upEndSection = findUpEndSection();
        stations.add(upEndSection.getUpStation());

        Section nextSection = upEndSection;
        while (Objects.nonNull(nextSection)) {
            stations.add(nextSection.getDownStation());
            nextSection = findSectionByNextUpStation(nextSection.getDownStation());
        }

        return stations;
    }

    private Section findUpEndSection() {
        List<Station> downStations = this.sections.stream()
            .map(Section::getDownStation)
            .collect(Collectors.toList());

        return this.sections.stream()
            .filter(it -> !downStations.contains(it.getUpStation()))
            .findFirst()
            .orElseThrow(() -> new ValidationFailureException("상행 종점역 찾기에 실패했습니다."));
    }

    private Section findSectionByNextUpStation(Station station) {
        return this.sections.stream()
            .filter(it -> it.getUpStation().equals(station))
            .findFirst()
            .orElse(null);
    }

    public void removeStation(Station station) {
        if (sections.size() < MINIMUM_STATIONS_IN_SECTIONS) {
            throw new ValidationFailureException(
                String.format("구간에는 최소 %s개의 역이 존재해야 합니다.", MINIMUM_STATIONS_IN_SECTIONS)
            );
        }

        Optional<Section> upSection = sections.stream()
            .filter(it -> it.getUpStation().equals(station))
            .findFirst();
        Optional<Section> downSection = sections.stream()
            .filter(it -> it.getDownStation().equals(station))
            .findFirst();

        if (upSection.isPresent() && downSection.isPresent()) {
            Station newUpStation = downSection.get().getUpStation();
            Station newDownStation = upSection.get().getDownStation();
            int newDistance = upSection.get().getDistance() + downSection.get().getDistance();
            sections.add(new Section(newUpStation, newDownStation, newDistance));
        }

        upSection.ifPresent(sections::remove);
        downSection.ifPresent(sections::remove);
    }

    public boolean contains(Section section) {
        return sections.contains(section);
    }

    public Stream<Section> toStream() {
        return sections.stream();
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }
}
