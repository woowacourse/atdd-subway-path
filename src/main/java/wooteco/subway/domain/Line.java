package wooteco.subway.domain;

import java.util.Collections;
import java.util.List;

public class Line {
    private Long id;
    private final String name;
    private final String color;
    private int extraFare;
    private Sections sections;

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
        this.sections = new Sections(Collections.emptyList());
    }

    public Line(Long id, String name, String color, int extraFare) {
        this(name, color);
        this.id = id;
        this.extraFare = extraFare;
    }

    public Line(Long id, String name, String color, int extraFare, Section section) {
        this(id, name, color, extraFare);
        this.sections = new Sections(List.of(section));
    }

    public Line(Long id, String name, String color, int extraFare, List<Section> sections) {
        this(id, name, color, extraFare);
        this.sections = new Sections(sections);
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public List<Section> findAll() {
        return sections.getSections();
    }

    public void deleteSections(Station station) {
        sections.delete(station);
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
}
