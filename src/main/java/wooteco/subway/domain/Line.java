package wooteco.subway.domain;

import java.util.LinkedList;
import java.util.List;

public class Line {

    private static final Integer DEFAULT_EXTRAFARE = 0;

    private final Long id;
    private final String name;
    private final String color;
    private final Sections sections;
    private final Integer extraFare;

    public Line(Long id, String name, String color, List<Section> sections, Integer extraFare) {
        validateNotNull(name, "name");
        validateNotNull(color, "color");
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = new Sections(sections);
        this.extraFare = extraFare;
    }

    public Line(String name, String color, Section section, Integer extraFare) {
        this(null, name, color, List.of(section), extraFare);
    }

    private void validateNotNull(String input, String param) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException(String.format("%s은 필수 입력값입니다.", param));
        }
    }

    public Line(String name, String color, Section section) {
        this(null, name, color, List.of(section), DEFAULT_EXTRAFARE);
    }

    public Line(Long id, String name, String color, int extraFare) {
        this(id, name, color, new LinkedList<>(), extraFare);
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
        return name;
    }

    public String getColor() {
        return color;
    }

    public Sections getSections() {
        return sections;
    }

    public List<Station> getStations() {
        return sections.getAllStations();
    }

    public Integer getExtraFare() {
        return extraFare;
    }
}
