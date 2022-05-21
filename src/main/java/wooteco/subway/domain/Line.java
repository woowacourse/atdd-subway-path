package wooteco.subway.domain;

import wooteco.subway.exception.DataLengthException;

import java.util.Objects;

public class Line {

    private static final int MAX_NAME_LENGTH = 20;
    private static final int MAX_COLOR_LENGTH = 20;

    private final Long id;
    private final String name;
    private final String color;
    private final int extraFare;

    public Line(String name, String color, int extraFare) {
        this(null, name, color, extraFare);
    }

    public Line(Long id, String name, String color, int extraFare) {
        validateDataSize(name, color);
        validatePositiveFare(extraFare);
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    private void validateDataSize(String name, String color) {
        if (name.isEmpty() || name.length() > MAX_NAME_LENGTH) {
            throw new DataLengthException("노선 이름이 빈 값이거나 최대 범위(" + MAX_NAME_LENGTH + ")를 초과했습니다.");
        }
        if (color.isEmpty() || color.length() > MAX_COLOR_LENGTH) {
            throw new DataLengthException("노선 색이 빈 값이거나 최대 범위(" + MAX_COLOR_LENGTH + "를 초과했습니다.");
        }
    }

    private void validatePositiveFare(int extraFare) {
        if (extraFare < 0) {
            throw new IllegalArgumentException("요금은 양의 정수만 가능합니다.");
        }
    }

    public boolean isSameLine(Long lineId) {
        return id.equals(lineId);
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return getExtraFare() == line.getExtraFare() &&
                getId().equals(line.getId()) &&
                getName().equals(line.getName()) &&
                getColor().equals(line.getColor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getColor(), getExtraFare());
    }
}
