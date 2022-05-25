package wooteco.subway.domain.vo;

import java.util.Objects;

public class LineColor {

    private final String color;

    private LineColor(String input) {
        final String name = String.valueOf(input);
        this.color = validate(name);
    }

    public static LineColor from(String input) {
        return new LineColor(input);
    }

    private String validate(String input) {
        validateNotNull(input);
        final String name = input.replaceAll(" ", "");
        validateLength(name);

        return name;
    }

    private void validateNotNull(String input) {
        if (Objects.isNull(input)) {
            throw new IllegalArgumentException("노선 색상은 null일 수 없습니다");
        }
    }

    private void validateLength(String name) {
        if (name.length() < 1 || 20 < name.length()) {
            throw new IllegalArgumentException("노선 색상의 길이는 1 이상, 20 이하여야 합니다");
        }
    }

    public String getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineColor that = (LineColor) o;
        return Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }
}
