package wooteco.subway.service.dto.request;

public class LineServiceRequest {
    private Long id;
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private Long extraFare;

    public LineServiceRequest(Long id, String name, String color, Long upStationId, Long downStationId, int distance,
                              Long extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public LineServiceRequest(Long id, String name, String color, Long upStationId, Long downStationId, int distance) {
        this(id, name, color, upStationId, downStationId, distance, null);
    }

    public LineServiceRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this(null, name, color, upStationId, downStationId, distance);
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public Long getExtraFare() {
        return extraFare;
    }
}
