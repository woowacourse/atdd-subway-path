package wooteco.subway.domain;

public class LineAndSection {

    private Long id;
    private String name;
    private String color;
    private Section section;

    public LineAndSection(Long id, String name, String color, Section section) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.section = section;
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

    public Section getSection() {
        return section;
    }
}
