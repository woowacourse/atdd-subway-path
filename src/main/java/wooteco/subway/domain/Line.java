package wooteco.subway.domain;

import java.util.Objects;

public class Line {

    public static final int MAX_NAME_LENGTH = 255;
    public static final int MAX_COLOR_LENGTH = 20;
    private Long id;
    private String name;
    private String color;
    private int extraFare;

    public Line() {
    }

    public Line(String name, String color, int extraFare) {
        this(null, name, color, extraFare);
    }

    public Line(Long id, String name, String color, int extraFare) {
        validateNameLength(name);
        validateColorLength(color);
        validateExtraFare(extraFare);

        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    private void validateNameLength(String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("노선 이름은 " + MAX_NAME_LENGTH + "자를 넘길 수 없습니다.");
        }
    }

    private void validateColorLength(String color) {
        if (color.length() > MAX_COLOR_LENGTH) {
            throw new IllegalArgumentException("노선 색깔은 " + MAX_COLOR_LENGTH + "자를 넘길 수 없습니다.");
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
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
