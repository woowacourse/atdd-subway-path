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
        validateArgument(name, color, extraFare);

        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    private void validateArgument(String name, String color, int extraFare) {
        if (name.isBlank() || color.isBlank()) {
            throw new IllegalArgumentException("노선의 이름 혹은 색이 공백일 수 없습니다.");
        }
        if (name.length() > 255 || color.length() > 20) {
            throw new IllegalArgumentException("노선의 이름이 255자 보다 크거나, 색이 20자 보다 큽니다.");
        }

        if (extraFare < 0) {
            throw new IllegalArgumentException("추가 요금은 0원 이상이어야합니다.");
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
        if (!(o instanceof Line)) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }
}
