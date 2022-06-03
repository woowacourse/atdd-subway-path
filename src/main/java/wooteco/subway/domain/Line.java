package wooteco.subway.domain;

import java.util.Objects;

public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final int extraFare;

    public Line(Long id, String name, String color, int extraFare) {
        validateColor(color);
        validateName(name);
        validateFare(extraFare);
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    private static void validateColor(String color) {
        if (color.isBlank()) {
            throw new IllegalArgumentException("올바른 색상을 입력해주세요.");
        }
    }

    private static void validateName(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("올바른 이름을 입력해주세요.");
        }
    }

    public Line(String name, String color) {
        this(null, name, color, 0);
    }

    public Line(String name, String color, int extraFare) {
        this(null, name, color, extraFare);
    }

    private static void validateFare(int extraFare) {
        if (extraFare < 0) {
            throw new IllegalArgumentException("추가요금은 음수일 수 없습니다.");
        }
    }

    public Line comparesMoreExpensiveExtraFare(Line line) {
        if (line == null) {
            return this;
        }
        if (extraFare >= line.extraFare) {
            return this;
        }
        return line;
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
