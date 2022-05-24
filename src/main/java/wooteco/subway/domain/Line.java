package wooteco.subway.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Line {
    private Long id;
    private Name name;
    private String color;
    private Sections sections;
    private int extraFare;

    private Line(Long id, Name name, String color, Sections sections, int extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
        this.extraFare = extraFare;
    }

    public static Line initialCreateWithoutId(String name, String color, Station upStation, Station downStation, int distance, int extraFare) {
        return new Line(null, new Name(name), color, new Sections(upStation, downStation, distance), extraFare);
    }

    public static Line createWithoutSection(Long id, String name, String color, int extraFare) {
        return new Line(id, new Name(name), color, null, extraFare);
    }

    public static Line createWithId(Long id, String name, String color, int extraFare, List<Section> sections) {
        return new Line(id, new Name(name), color, new Sections(sections), extraFare);
    }

    public void addSection(Section section) {
        sections.addSection(section);
    }

    public void deleteSection(Station station) {
        sections.deleteSection(station);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections.getSections());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public String getColor() {
        return color;
    }

    public int getExtraFare() {
        return extraFare;
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
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects
                .equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
