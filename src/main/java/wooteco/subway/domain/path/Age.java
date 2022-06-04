package wooteco.subway.domain.path;

public class Age {
    private final int value;

    public Age(int value) {
        checkNegative(value);
        this.value = value;
    }

    private void checkNegative(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("나이는 음수일 수 없습니다.");
        }
    }

    boolean isSameOrBiggerThan(int age) {
        return this.value == age || this.value > age;
    }

    boolean isSmallerThan(int age) {
        return this.value < age;
    }
}
