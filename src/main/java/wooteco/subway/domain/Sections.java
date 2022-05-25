package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import wooteco.subway.exception.IllegalInputException;
import wooteco.subway.exception.section.NoSuchSectionException;

public class Sections {

    private static final int MIN_SECTION_SIZE = 1;
    private static final int MERGE_REQUIRED_SIZE = 2;

    private final List<Section> values;

    public Sections(final List<Section> values) {
        this.values = values;
    }

    public Sections findDeletableSections(final Station stationToDelete) {
        validateMinSize();
        final List<Section> deletableSections = values
                .stream()
                .filter(it -> it.contains(stationToDelete))
                .collect(Collectors.toList());
        if (deletableSections.isEmpty()) {
            throw new NoSuchSectionException();
        }
        return new Sections(deletableSections);
    }

    private void validateMinSize() {
        if (values.size() == MIN_SECTION_SIZE) {
            throw new IllegalInputException("구간을 삭제할 수 없습니다.");
        }
    }

    public boolean needMerge() {
        return values.size() == MERGE_REQUIRED_SIZE;
    }

    public Section toMergedSection() {
        final Section first = values.get(0);
        final Section second = values.get(1);
        return first.merge(second);
    }

    public List<Station> toStation() {
        Station upStation = findEndUpStation();

        final List<Station> stations = new ArrayList<>();
        stations.add(upStation);
        while (stations.size() != values.size() + 1) {
            final Section section = findSectionByUpStation(upStation);
            upStation = section.getDownStation();
            stations.add(upStation);
        }
        return stations;
    }

    private Station findEndUpStation() {
        if (values.size() < MIN_SECTION_SIZE) {
            throw new NoSuchSectionException();
        }
        final List<Station> upStations = values.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
        final List<Station> downStations = values.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
        upStations.removeAll(downStations);
        return upStations.get(0);
    }

    private Section findSectionByUpStation(final Station upStation) {
        return values
                .stream()
                .filter(it -> it.hasSameUpStation(upStation))
                .findFirst()
                .orElseThrow(NoSuchSectionException::new);
    }

    public List<Section> getValues() {
        return values;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Sections that = (Sections) o;
        return Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

    @Override
    public String toString() {
        return "Sections{" +
                "values=" + values +
                '}';
    }
}
