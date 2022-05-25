package wooteco.subway.domain.line;

import java.util.Objects;
import java.util.regex.Pattern;

public class Line {

    private static final Pattern pattern = Pattern.compile("^[ㄱ-ㅎ|가-힣|0-9]+");
    private static final int MAX_RANGE = 10;
    private static final int MIN_RANGE = 3;
    private static final int MIN_EXTRA_FARE = 0;
    private static final int DEFAULT_EXTRA_FARE = 0;

    private final Long id;
    private String name;
    private String color;
    private int extraFare;

    public Line(Long id, String name, String color, int extraFare) {
        validate(name, color, extraFare);
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public Line(long id, String color, String green) {
        this(id, color, green, DEFAULT_EXTRA_FARE);
    }

    public Line(String name, String color) {
        this(null, name, color, DEFAULT_EXTRA_FARE);
    }

    public Line(String name, String color, int extraFare) {
        this(null, name, color, extraFare);
    }

    public void update(String name, String color, int extraFare) {
        validate(name, color, extraFare);
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    private void validate(String name, String color, int extraFare) {
        validateEmpty(name, color);
        validateNameRange(name);
        validateLanguageType(name);
        validateExtraFare(extraFare);
    }

    private void validateEmpty(String name, String color) {
        if (name.isBlank() || color.isBlank()) {
            throw new IllegalArgumentException("이름과 색깔은 공백일 수 없습니다.");
        }
    }

    private void validateNameRange(String name) {
        if (name.length() >= MAX_RANGE) {
            throw new IllegalArgumentException(
                    String.format("노선 이름은 %d글자를 초과할 수 없습니다.", MAX_RANGE));
        }

        if (name.length() < MIN_RANGE) {
            throw new IllegalArgumentException(String.format("노선 이름은 %d글자 이상이어야 합니다.", MIN_RANGE));
        }
    }

    private void validateLanguageType(String name) {
        if (!pattern.matcher(name).matches()) {
            throw new IllegalArgumentException("노선 이름은 한글과 숫자이어야 합니다.");
        }
    }

    private void validateExtraFare(int extraFare) {
        if (extraFare < MIN_EXTRA_FARE) {
            throw new IllegalArgumentException("추가 금액은 음수일 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getExtraFare() {
        return extraFare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Line)) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
