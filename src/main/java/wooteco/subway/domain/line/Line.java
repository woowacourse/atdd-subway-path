package wooteco.subway.domain.line;

import java.util.List;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;

public class Line {
    private Long id;
    private String name;
    private String color;
    private Sections sections;

    private Line() {
    }

    public Line(Long id, String name, String color, Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public Line(String name, String color, Section section) {
        this(null, name, color, new Sections(List.of(section)));
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

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
