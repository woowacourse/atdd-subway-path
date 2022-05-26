package wooteco.subway.domain.line;

import java.util.List;
import java.util.Objects;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.domain.station.Station;

public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final LineExtraFare extraFare;
    private final Sections sections;

    private Line(Long id, String name, String color, LineExtraFare extraFare, Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.sections = sections;
    }

    public Line(Long id, String name, String color, int extraFare, Sections sections) {
        this(id, name, color, new LineExtraFare(extraFare), sections);
    }

    public Line(String name, String color, int extraFare, Section section) {
        this(null, name, color, extraFare, new Sections(section));
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
        return extraFare.getValue();
    }

    public Sections getSections() {
        return sections;
    }

    public List<Station> getSortedStations() {
        return sections.toSortedStations();
    }

    public List<Section> toSectionList() {
        return sections.toSortedList();
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
        return Objects.equals(id, line.id)
                && Objects.equals(name, line.name)
                && Objects.equals(color, line.color)
                && Objects.equals(extraFare, line.extraFare)
                && Objects.equals(sections, line.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, extraFare, sections);
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
