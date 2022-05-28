package wooteco.subway.domain.path;

public class Age {

    private final long age;

    public Age(long age) {
        validateAgePositive(age);
        this.age = age;
    }

    private void validateAgePositive(long age) {
        if (age <= 0) {
            throw new IllegalArgumentException("나이는 양수여야 합니다.");
        }
    }

    public boolean isLowerThan(long age) {
        return this.age < age;
    }

    public boolean isSameOrHigherThan(long age) {
        return age <= this.age;
    }

    public long getAge() {
        return age;
    }
}
