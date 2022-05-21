package wooteco.subway.dto;

import wooteco.subway.exception.PositiveDigitException;

public class LineRequest {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int extraFare;

    private LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, int extraFare) {
        validatePositive(distance, extraFare);
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    private void validatePositive(final int distance, final int extraFare) {
        validatePositiveDistance(distance);
        validatePositiveExtraFare(extraFare);
    }

    private void validatePositiveDistance(final int distance) {
        if (distance <= 0) {
            throw new PositiveDigitException("구간의 길이가 양수가 아닙니다.");
        }
    }

    private void validatePositiveExtraFare(final int extraFare) {
        if (extraFare <= 0) {
            throw new PositiveDigitException("추가 요금이 양수가 아닙니다.");
        }
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public int getExtraFare() {
        return extraFare;
    }
}
