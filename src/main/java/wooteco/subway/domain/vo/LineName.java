package wooteco.subway.domain.vo;

import java.util.Objects;

public class LineName {

    private final String name;

    private LineName(String input) {
        final String name = String.valueOf(input);
        this.name = validate(name);
    }

    public static LineName from(String input) {
        return new LineName(input);
    }

    private String validate(String input) {
        validateNotNull(input);
        final String name = input.replaceAll(" ", "");
        validateLength(name);

        return name;
    }

    private void validateNotNull(String input) {
        if (Objects.isNull(input)) {
            throw new IllegalArgumentException("노선 이름은 null일 수 없습니다");
        }
    }

    private void validateLength(String name) {
        if (name.length() < 1 || 255 < name.length()) {
            throw new IllegalArgumentException("노선 이름의 길이는 1 이상, 255 이하여야 합니다");
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineName that = (LineName) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
