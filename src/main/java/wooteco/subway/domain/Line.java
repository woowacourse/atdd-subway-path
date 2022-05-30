package wooteco.subway.domain;

import java.util.List;
import java.util.Objects;
import wooteco.subway.exception.ExceptionMessage;
import wooteco.subway.exception.domain.LineException;

public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final Sections sections;
    private final int extraFare;

    public Line(Long id, String name, String color, List<Section> sections, int extraFare) {
        if (name.isBlank()) {
            throw new LineException(ExceptionMessage.BLANK_LINE_NAME.getContent());
        }
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = new Sections(sections);
        this.extraFare = extraFare;
    }

    public Line(String name, String color, List<Section> sections, int extraFare) {
        this(null, name, color, sections, extraFare);
    }

    public void add(Section section) {
        sections.add(section);
    }

    public List<Station> getSortedStations() {
        return sections.getSortedStation();
    }

    public void deleteSectionNearBy(Station station) {
        sections.deleteNearBy(station);
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

    public List<Section> getSections() {
        return sections.getValue();
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
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
