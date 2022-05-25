package wooteco.subway.dto.service.request;

public class LineUpdateRequest {
    private long id;
    private String name;
    private String color;
    private int extraFare;

    public LineUpdateRequest(long id, String name, String color, int extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public long getId() {
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
