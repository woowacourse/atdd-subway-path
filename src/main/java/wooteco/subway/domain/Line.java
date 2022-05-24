package wooteco.subway.domain;

import java.util.LinkedList;
import java.util.List;

public class Line {

    private final Long id;
    private final Name name;
    private final Color color;
    private final int extraFare;
    private final Sections sections;

    public Line(Long id, Line line) {
        this(id, new Name(line.getName()), new Color(line.getColor()), line.extraFare, line.getSections());
    }

    public Line(String name, String color, int extraFare, Section section) {
        this(null, name, color, extraFare, List.of(section));
    }

    public Line(Long id, String name, String color, int extraFare) {
        this(id, name, color, extraFare, new LinkedList<>());
    }

    public Line(Long id, String name, String color, int extraFare, List<Section> sections) {
        this(id, new Name(name), new Color(color), extraFare, new Sections(sections));
    }

    public Line(Long id, Name name, Color color, int extraFare, Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.sections = sections;
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public Section delete(Station station) {
        return sections.delete(station);
    }

    public boolean hasSameNameWith(Line otherLine) {
        return this.name.equals(otherLine.name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public String getColor() {
        return color.getValue();
    }

    public Sections getSections() {
        return sections;
    }

    public List<Station> getStations() {
        return sections.getAllStations();
    }

    public int getExtraFare() {
        return extraFare;
    }
}
