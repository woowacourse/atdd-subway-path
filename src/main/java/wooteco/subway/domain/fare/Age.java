package wooteco.subway.domain.fare;

import java.util.Objects;
import wooteco.subway.exception.NegativeAgeException;

public class Age {

    private static final int BABY_MAX_AGE = 5;
    private static final int CHILDREN_MIN_AGE = 6;
    private static final int CHILDREN_MAX_AGE = 12;
    private static final int TEENAGER_MIN_AGE = 13;
    private static final int TEENAGER_MAX_AGE = 18;
    private static final int ADULT_MIN_AGE = 19;
    private static final int SENIOR_MIN_AGE = 65;

    private final int value;

    public Age(int value) {
        validatePositiveAge(value);
        this.value = value;
    }

    private void validatePositiveAge(int value) {
        if (value < 0) {
            throw new NegativeAgeException();
        }
    }

    public boolean isBabyOrSenior() {
        return value <= BABY_MAX_AGE || SENIOR_MIN_AGE <= value;
    }

    public boolean isChildren() {
        return CHILDREN_MIN_AGE <= value && value <= CHILDREN_MAX_AGE;
    }

    public boolean isTeenager() {
        return TEENAGER_MIN_AGE <= value && value <= TEENAGER_MAX_AGE;
    }

    public boolean isAdult() {
        return ADULT_MIN_AGE <= value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Age age = (Age) o;
        return value == age.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Age{" +
                "value=" + value +
                '}';
    }
}
