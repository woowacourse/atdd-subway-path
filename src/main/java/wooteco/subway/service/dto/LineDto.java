package wooteco.subway.service.dto;

public class LineDto {

    private static final int DEFAULT_EXTRA_FARE = 0;

    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;
    private final int extraFare;

    public LineDto(final String name, final String color, final Long upStationId, final Long downStationId, final int distance) {
        this(name, color, upStationId, downStationId, distance, DEFAULT_EXTRA_FARE);
    }

    public LineDto(final String name, final String color, final Long upStationId, final Long downStationId,
                   final int distance, final int extraFare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraFare = extraFare;
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

    @Override
    public String toString() {
        return "LineDto{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", upStationId=" + upStationId +
                ", downStationId=" + downStationId +
                ", distance=" + distance +
                ", extraFare=" + extraFare +
                '}';
    }
}
