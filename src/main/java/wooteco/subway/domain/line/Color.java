package wooteco.subway.domain.line;

public class Color {

    private final String value;

    public Color(String value) {
        validateNotNull(value);
        this.value = value;
    }

    private void validateNotNull(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("색상은 필수 입력값입니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
