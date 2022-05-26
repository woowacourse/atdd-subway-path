package wooteco.subway.domain;

import java.util.List;
import java.util.Objects;

public class Lines {

    private final List<Line> values;

    public Lines(List<Line> values) {
        validate(values);
        this.values = values;
    }

    private void validate(List<Line> values) {
        validateNotNull(values);
        validateDuplicate(values);
    }

    private void validateNotNull(List<Line> values) {
        if (values == null) {
            throw new IllegalArgumentException("노선 목록은 null일 수 없습니다.");
        }
        if (values.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("노선 목록은 null을 포함할 수 없습니다.");
        }
    }

    private void validateDuplicate(List<Line> values) {
        int distinctSize = (int) values.stream()
                .distinct()
                .count();
        if (values.size() != distinctSize) {
            throw new IllegalArgumentException("노선 목록에 중복된 노선이 있습니다.");
        }
    }

    public int getMostExpensiveExtraFare() {
        return values.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
    }

    public List<Line> getValues() {
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lines lines = (Lines) o;
        return Objects.equals(values, lines.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

}
