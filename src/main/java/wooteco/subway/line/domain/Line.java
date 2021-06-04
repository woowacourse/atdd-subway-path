package wooteco.subway.line.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import wooteco.subway.station.domain.Station;

public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final Sections sections;

    public Line(String name, String color) {
        this(null, name, color, new Sections());
    }

    public Line(Long id, String name, String color) {
        this(id, name, color, new Sections());
    }

    public Line(Long id, String name, String color, Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        Section section = new Section(upStation, downStation, distance);
        sections.addSection(section);
    }

    public void addSection(Section section) {
        if (Objects.isNull(section)) {
            return;
        }
        sections.addSection(section);
    }

    public void removeSection(Station station) {
        sections.removeStation(station);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public boolean contains(Section section) {
        return sections.contains(section);
    }

    public Stream<Station> toStationStream() {
        return getStations().stream();
    }

    public Stream<Section> toSectionStream() {
        return sections.toStream();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Sections getSections() {
        return sections;
    }
}
