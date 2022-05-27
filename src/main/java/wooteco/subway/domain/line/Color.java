package wooteco.subway.domain.line;

public class Color {
    private static final String ERROR_NULL_OR_EMPTY = "색상은 필수 입력값입니다.";

    private final String value;

    public Color(String value) {
        validateNotNull(value);
        this.value = value;
    }

    private void validateNotNull(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(ERROR_NULL_OR_EMPTY);
        }
    }

    public String getValue() {
        return value;
    }
}
