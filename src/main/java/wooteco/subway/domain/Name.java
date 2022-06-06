package wooteco.subway.domain;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Name {
    private static final Pattern REGEX = Pattern.compile(".*[^0-9a-zA-Zㄱ-ㅎ가-힣_]+.*");

    private final String value;

    public Name(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        checkNull(value);
        checkEmpty(value);
        checkSpecialChar(value);
    }

    private void checkNull(String value) {
        if (value == null) {
            throw new IllegalArgumentException("이름은 null이 될 수 없습니다.");
        }
    }

    private void checkEmpty(String value) {
        if (value.contains(" ")) {
            throw new IllegalArgumentException("이름에 공백이 포함될 수 없습니다.");
        }
    }

    private void checkSpecialChar(String value) {
        Matcher matcher = REGEX.matcher(value);
        if (matcher.find()) {
            throw new IllegalArgumentException("이름에 특수문자가 포함될 수 없습니다.");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Name name = (Name)o;
        return value.equals(name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
