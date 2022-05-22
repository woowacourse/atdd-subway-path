package wooteco.subway.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final Long extraFare;
    private final Sections sections;

    public Line(Long id, String name, String color, Long extraFare, Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.sections = sections;
    }

    public Line(Long id, String name, String color, Sections sections) {
        this(id, name, color, null, sections);
    }

    public Line(Long id, String name, String color) {
        this(id, name, color, null);
    }

    public Line(String name, String color, Sections sections) {
        this(null, name, color, sections);
    }

    public static Line of(Line line, Sections sections) {
        return new Line(line.getId(), line.getName(), line.getColor(), line.getExtraFare(), sections);
    }

    public List<Station> getStations() {
        return sections.getSections()
                .stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .distinct()
                .collect(Collectors.toList());
    }

    public boolean isEmptyStations() {
        return Objects.isNull(sections);
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

    public Long getExtraFare() {
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
        return Objects.equals(name, line.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
