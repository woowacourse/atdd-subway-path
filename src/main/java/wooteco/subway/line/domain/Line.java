package wooteco.subway.line.domain;

import wooteco.subway.line.domain.section.Section;
import wooteco.subway.line.domain.section.Sections;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.Objects;

public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final Sections sections;

    public Line(Long id, String name, String color, Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public Line(Long id, String name, String color) {
        this(id, name, color, Sections.empty());
    }

    public Line(String name, String color) {
        this(null, name, color, Sections.empty());
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

    public void addSection(Station upStation, Station downStation, int distance) {
        Section section = new Section(upStation, downStation, distance);

        sections.addSection(section);
    }

    public void addSection(Section section) {
        Objects.requireNonNull(section);

        sections.addSection(section);
    }

    public void removeSection(Station station) {
        sections.removeStation(station);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

}
