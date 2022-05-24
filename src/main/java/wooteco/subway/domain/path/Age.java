package wooteco.subway.domain.path;

public class Age {

    private static final int MINIMUM_AGE = 1;
    private final int value;

    public Age(int value) {
        this.value = value;
        validatePositiveNumber();
    }

    private void validatePositiveNumber() {
        if (value < MINIMUM_AGE) {
            throw new IllegalArgumentException("나이는 양수여야 합니다.");
        }
    }

    public boolean lessThan(int value) {
        return this.value < value;
    }

    public boolean moreThan(int value) {
        return this.value >= value;
    }
}
