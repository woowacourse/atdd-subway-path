package wooteco.subway.domain.line;

import java.util.Objects;

public class LineInfo {

    private static final int MIN_EXTRA_FARE = 0;
    private static final String INVALID_EXTRA_FARE_VALUE_EXCEPTION = "노선 추가비용은 0원 이상이어야 합니다.";

    private final Long id;
    private final String name;
    private final String color;
    private final int extraFare;

    public LineInfo(Long id, String name, String color, int extraFare) {
        validateExtraFare(extraFare);
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public LineInfo(String name, String color, int extraFare) {
        this(null, name, color, extraFare);
    }

    private void validateExtraFare(int extraFare) {
        if (extraFare < MIN_EXTRA_FARE) {
            throw new IllegalArgumentException(INVALID_EXTRA_FARE_VALUE_EXCEPTION);
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
        LineInfo lineInfo = (LineInfo) o;
        return extraFare == lineInfo.extraFare
                && Objects.equals(id, lineInfo.id)
                && Objects.equals(name, lineInfo.name)
                && Objects.equals(color, lineInfo.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, extraFare);
    }

    @Override
    public String toString() {
        return "LineInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", extraFare=" + extraFare +
                '}';
    }
}
