package wooteco.subway.domain.line;

import java.util.List;

import wooteco.subway.domain.Id;
import wooteco.subway.domain.line.section.OrderedSections;
import wooteco.subway.domain.line.section.Section;
import wooteco.subway.domain.line.section.SectionSorter;

public class Line {

    private final Id id;
    private final OrderedSections sections;
    private LineName name;
    private LineColor color;

    public Line(Id id, List<Section> sections, LineName name, LineColor color) {
        this.id = id;
        this.sections = new OrderedSections(sections, new SectionSorter());
        this.name = name;
        this.color = color;
    }

    public Line(long id, List<Section> sections, String name, String color) {
        this(new Id(id), sections, new LineName(name), new LineColor(color));
    }

    public Line(List<Section> sections, String name, String color) {
        this(Id.temporary(), sections, new LineName(name), new LineColor(color));
    }

    public void update(String name, String color) {
        this.name = new LineName(name);
        this.color = new LineColor(color);
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
}
