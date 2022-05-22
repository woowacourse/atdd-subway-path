package wooteco.subway.domain;

import wooteco.subway.exception.PositiveDigitException;

public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final int extraFare;

    public Line(String name, String color, int extraFare) {
        this(0L, name, color, extraFare);
    }

    public Line(Long id, String name, String color, int extraFare) {
        validatePositiveExtraFare(extraFare);
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    private void validatePositiveExtraFare(final int extraFare) {
        if (extraFare <= 0) {
            throw new PositiveDigitException("추가 요금이 양수가 아닙니다.");
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
