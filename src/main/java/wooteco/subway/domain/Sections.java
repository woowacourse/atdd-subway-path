package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

public class Sections {

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sort(sections);
    }

    public List<Station> getStations() {
        Set<Station> stations = new LinkedHashSet<>();
        for (Section section : sections) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }
        return new ArrayList<>(stations);
    }

    public Station getLastUpStation(List<Section> sections) {
        List<Station> upStations = getUpStations(sections);
        List<Station> downStations = getDownStations(sections);
        return upStations.stream()
                .filter(station -> !downStations.contains(station))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("상행종점이 없습니다."));
    }

    public Station getLastDownStation(List<Section> sections) {
        List<Station> upStations = getUpStations(sections);
        List<Station> downStations = getDownStations(sections);
        return downStations.stream()
                .filter(station -> !upStations.contains(station))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("상행종점이 없습니다."));
    }

    public boolean isSplit(Station upStation, Station downStation) {
        return hasSameUpStation(upStation) || hasSameDownStation(downStation);
    }

    public boolean hasSameUpStation(Station station) {
        return sections.stream()
                .anyMatch(section -> section.isSameUpStation(station));
    }

    public boolean hasSameDownStation(Station station) {
        return sections.stream()
                .anyMatch(section -> section.isSameDownStation(station));
    }

    public boolean hasOnlyOneSection() {
        return sections.size() == 1;
    }

    public boolean hasStation(Station station) {
        return getStations().contains(station);
    }

    public List<Section> getSections() {
        return sections;
    }

    public Section splitSection(Section newSection) {
        Station upStation = newSection.getUpStation();
        Station downStation = newSection.getDownStation();
        int distance = newSection.getDistance();

        if (hasSameUpStation(upStation)) {
            Section existSection = findSectionWithUpStation(upStation);
            int existDistance = existSection.getDistance();
            return new Section(existSection.getId(), newSection.getLine(), downStation,
                    existSection.getDownStation(), existDistance - distance);
        }

        Section existSection = findSectionWithDownStation(downStation);
        int existDistance = existSection.getDistance();
        return new Section(existSection.getId(), newSection.getLine(),
                existSection.getUpStation(),
                upStation, existDistance - distance);
    }

    private List<Section> sort(List<Section> sections) {
        List<Section> sortedSection = new ArrayList<>();
        Section next = findFirstSection(sections);
        while (next != null) {
            sortedSection.add(next);
            Station downStation = next.getDownStation();
            next = findNext(sections, downStation);
        }
        return sortedSection;
    }

    private List<Station> getUpStations(List<Section> sections) {
        return sections.stream()
                .map(section -> section.getUpStation())
                .collect(Collectors.toList());
    }

    private List<Station> getDownStations(List<Section> sections) {
        return sections.stream()
                .map(section -> section.getDownStation())
                .collect(Collectors.toList());
    }

    private Section findSectionWithUpStation(Station upStation) {
        return sections.stream()
                .filter(section -> section.isSameUpStation(upStation))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 상행역을 가지는 구간이 없습니다."));
    }

    private Section findSectionWithDownStation(Station downStation) {
        return sections.stream()
                .filter(section -> section.isSameDownStation(downStation))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 하행역을 가지는 구간이 없습니다."));
    }

    private Section findNext(List<Section> sections, Station downStation) {
        return sections.stream()
                .filter(section -> section.isSameUpStation(downStation))
                .findFirst()
                .orElse(null);
    }

    private Section findFirstSection(List<Section> sections) {
        Station lastUpStation = getLastUpStation(sections);
        return sections.stream()
                .filter(section -> section.isSameUpStation(lastUpStation))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("상행종점이 없습니다."));
    }
}

