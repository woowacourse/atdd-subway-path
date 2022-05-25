package wooteco.subway.domain;

public class Line {

    private static final int MONEY_UNIT = 10;

    private Long id;
    private String name;
    private String color;
    private int extraFare;

    private Line() {
    }

    private Line(final Long id, final String name, final String color, final int extraFare) {
        validateExtraFare(extraFare);
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public static Line of(final String name, final String color, final int extraFare) {
        return Line.of(null, name, color, extraFare);
    }

    public static Line of(final Long id, final String name, final String color, final int extraFare) {
        return new Line(id, name, color, extraFare);
    }

    private static void validateExtraFare(final int extraFare) {
        validateExtraFarePositive(extraFare);
        validateExtraFareMoneyUnit(extraFare);
    }

    private static void validateExtraFarePositive(int extraFare) {
        if (extraFare < 0) {
            throw new IllegalArgumentException("추가요금은 0 이상이어야 합니다.");
        }
    }

    private static void validateExtraFareMoneyUnit(int extraFare) {
        if (extraFare % MONEY_UNIT != 0) {
            throw new IllegalArgumentException("추가요금은 10원 단위로 입력해주세요.");
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
