package wooteco.subway.domain;

import java.util.List;

public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final int extraFare;
    private final Sections sections;

    public Line(final String name, final String color, final int extraFare) {
        this(null, name, color, extraFare, new Sections());
    }

    public Line(final String name, final String color, final int extraFare, final Sections sections) {
        this(null, name, color, extraFare, sections);
    }

    public Line(final Long id, final String name, final String color, final int extraFare) {
        this(id, name, color, extraFare, new Sections());
    }

    public Line(final Long id, final String name, final String color, final int extraFare, final Sections sections) {
        validateName(name);
        validateColor(color);
        validateExtraFare(extraFare);
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.sections = sections;
    }

    private void validateName(final String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("노선 이름은 공백일 수 없습니다.");
        }
    }

    private void validateColor(final String color) {
        if (color.isBlank()) {
            throw new IllegalArgumentException("색상이 공백일 수 없습니다.");
        }
    }

    private void validateExtraFare(final int extraFare) {
        if (extraFare < 0) {
            throw new IllegalArgumentException("추가 요금은 0 이상의 정수여야합니다.");
        }
    }

    public boolean contains(final Section section) {
        return sections.contains(section);
    }

    public List<Station> getSortedStations() {
        return sections.toSortedStations();
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
