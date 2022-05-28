package wooteco.subway.domain;

public class Line {

    private static final int EXTRA_FARE_MIN_VALUE = 100;

    private Long id;
    private final String name;
    private final String color;
    private final int extraFare;

    public Line(Long id, String name, String color, int extraFare) {
        validateExtraFare(extraFare);
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public Line(String name, String color, int extraFare) {
        this(null, name, color, extraFare);
    }

    private void validateExtraFare(int extraFare) {
        if (extraFare < EXTRA_FARE_MIN_VALUE) {
            throw new IllegalArgumentException("노선의 최소 추가 금액은 100원입니다.");
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
}
