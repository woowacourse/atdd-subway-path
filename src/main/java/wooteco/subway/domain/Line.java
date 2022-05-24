package wooteco.subway.domain;

import java.util.List;
import java.util.Objects;

public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final int extraFare;
    private Sections sections = new Sections();

    public Line(Long id, String name, String color, int extraFare,
        Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.sections = sections;
    }

    public Line(Long id, String name, String color, int extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public Line(String name, String color, int extraFare) {
        this(null, name, color, extraFare);
    }

    public void addSection(Section section) {
        sections.addSection(section);
    }

    public void addSection(Station upStation, Station downStation, Integer distance) {
        Section section = new Section(upStation, downStation, distance);
        sections.addSection(section);
    }

    public void removeSection(Station station) {
        sections.removeStation(station);
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

    public int getExtraFare() {
        return extraFare;
    }

    public Sections getSections() {
        return sections;
    }

    public List<Station> getStations() {
        return sections.findStations();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return getExtraFare() == line.getExtraFare() && Objects.equals(getId(), line.getId())
            && Objects.equals(getName(), line.getName()) && Objects.equals(
            getColor(), line.getColor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getColor(), getExtraFare());
    }

    @Override
    public String toString() {
        return "Line{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", color='" + color + '\'' +
            ", extraFare=" + extraFare +
            ", sections=" + sections +
            '}';
    }
}
