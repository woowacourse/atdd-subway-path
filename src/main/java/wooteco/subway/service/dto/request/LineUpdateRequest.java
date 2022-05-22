package wooteco.subway.service.dto.request;

public class LineUpdateRequest {

    private Long id;
    private String name;
    private String color;
    private int extraFare;


    public LineUpdateRequest() {}

    public LineUpdateRequest(Long id, String name, String color, int extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
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
