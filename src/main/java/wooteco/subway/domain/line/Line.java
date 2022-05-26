package wooteco.subway.domain.line;

import java.util.List;

import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Id;
import wooteco.subway.domain.line.section.OrderedSections;
import wooteco.subway.domain.line.section.Section;
import wooteco.subway.domain.line.section.SectionSorter;

public class Line {

    private final Id id;
    private final OrderedSections sections;
    private LineName name;
    private LineColor color;
    private Fare extraFare;

    public Line(Id id, List<Section> sections, LineName name, LineColor color, Fare extraFare) {
        this.id = id;
        this.sections = new OrderedSections(sections, new SectionSorter());
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public Line(long id, List<Section> sections, String name, String color, long extraFare) {
        this(new Id(id), sections, new LineName(name), new LineColor(color), new Fare(extraFare));
    }

    public Line(List<Section> sections, String name, String color, long extraFare) {
        this(Id.temporary(), sections, new LineName(name), new LineColor(color), new Fare(extraFare));
    }

    public void update(String name, String color, long extraFare) {
        this.name = new LineName(name);
        this.color = new LineColor(color);
        this.extraFare = new Fare(extraFare);
    }

    public void appendSection(Section section) {
        sections.append(section);
    }

    public void removeStation(long stationId) {
        sections.remove(stationId);
    }

    public long getId() {
        return id.getId();
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public String getName() {
        return name.getName();
    }

    public String getColor() {
        return color.getColor();
    }

    public long getExtraFare() {
        return extraFare.getFare();
    }
}
