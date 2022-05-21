package wooteco.subway.domain;

import java.util.Objects;

public class Line {

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
        validateExtraFare(extraFare);

        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
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
