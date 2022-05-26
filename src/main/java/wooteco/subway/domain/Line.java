package wooteco.subway.domain;

import java.util.Objects;

public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final int extraFare;

    public Line(String name, String color, int extraFare) {
        this(null, name, color, extraFare);
    }

    public Line(Long id, String name, String color, int extraFare) {
        validate(name, color, extraFare);
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    private void validate(String name, String color, int extraFare) {
        validateName(name);
        validateColor(color);
        validateExtraFare(extraFare);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("노선 이름은 빈 값일 수 없습니다.");
        }
    }

    private void validateColor(String color) {
        if (color == null || color.isBlank()) {
            throw new IllegalArgumentException("노선 색상은 빈 값일 수 없습니다.");
        }
    }

    private void validateExtraFare(int extraFare) {
        if (extraFare < 0) {
            throw new IllegalArgumentException("추가 요금은 음수일 수 없습니다.");
        }
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
        return extraFare == line.extraFare && Objects.equals(id, line.id) && Objects.equals(name,
                line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, extraFare);
    }

}
