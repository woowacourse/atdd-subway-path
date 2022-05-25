package wooteco.subway.domain.vo;

import java.util.Objects;

public class LineId {

    private final Long id;

    private LineId(Long input) {
        this.id = validate(input);
    }

    public static LineId from(Long input) {
        return new LineId(input);
    }

    private Long validate(Long input) {
        validatePositive(input);

        return input;
    }

    private void validatePositive(Long input) {
        if (Objects.nonNull(input) && input < 1) {
            throw new IllegalArgumentException("노선 아이디는 1 이상이어야 합니다 : " + input);
        }
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineId that = (LineId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
