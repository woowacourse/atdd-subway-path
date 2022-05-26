package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Stations {

    private final List<Station> values;

    public Stations(List<Station> values) {
        validate(values);
        this.values = new ArrayList<>(values);
    }

    private void validate(List<Station> values) {
        validateNotNull(values);
        validateDuplicate(values);
    }

    private void validateNotNull(List<Station> values) {
        if (values == null) {
            throw new IllegalArgumentException("역 목록은 null일 수 없습니다.");
        }
        if (values.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("역 목록은 null을 포함할 수 없습니다.");
        }
    }

    private void validateDuplicate(List<Station> values) {
        int distinctSize = (int) values.stream()
                .distinct()
                .count();
        if (values.size() != distinctSize) {
            throw new IllegalArgumentException("역 목록에 중복된 역이 있습니다.");
        }
    }

    public List<Station> getValues() {
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
        Stations stations = (Stations) o;
        return Objects.equals(values, stations.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }
}
