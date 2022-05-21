package wooteco.subway.domain;

import java.util.Collections;
import java.util.List;

public class Line {
    private static final int DEFAULT_EXTRA_FARE = 0;

    private final Long id;
    private final String name;
    private final String color;
    private final int extraFare;
    private final Sections sections;

    public Line(Long id, String name, String color, int extraFare, List<Section> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.sections = new Sections(sections);
    }

    public Line(String name, String color) {
        this(null, name, color, DEFAULT_EXTRA_FARE, Collections.emptyList());
    }

    public Line(String name, String color, int extraFare) {
        this(null, name, color, extraFare, Collections.emptyList());
    }

    public Line(Long id, String name, String color, int extraFare) {
        this(id, name, color, extraFare, Collections.emptyList());
    }

    public Line(Long id, String name, String color, int extraFare, Section section) {
        this(id, name, color, extraFare, List.of(section));
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
