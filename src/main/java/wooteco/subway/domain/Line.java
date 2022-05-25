package wooteco.subway.domain;

public class Line {

    private static final int EXTRA_FARE_MIN_VALUE = 100;

    private Long id;
    private final String name;
    private final String color;
    private final Integer extraFare;

    public Line(Long id, String name, String color, Integer extraFare) {
        validateExtraFare(extraFare);
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public Line(String name, String color, Integer extraFare) {
        this(null, name, color, extraFare);
    }

    private void validateExtraFare(Integer extraFare) {
        if (extraFare < EXTRA_FARE_MIN_VALUE) {
            throw new IllegalArgumentException("최소 추가 금액은 100원입니다.");
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

    public Integer getExtraFare() {
        return extraFare;
    }
}
