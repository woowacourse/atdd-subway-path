package wooteco.subway.domain.line;

import wooteco.subway.exception.PositiveDigitException;

public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final int extraFare;

    public Line(final String name, final String color, final int extraFare) {
        this(0L, name, color, extraFare);
    }

    public Line(final Long id, final String name, final String color, final int extraFare) {
        validatePositiveExtraFare(extraFare);
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    private void validatePositiveExtraFare(final int extraFare) {
        if (extraFare < 0) {
            throw new PositiveDigitException("추가 요금이 0보다 작습니다.");
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
