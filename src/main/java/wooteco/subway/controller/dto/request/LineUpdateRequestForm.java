package wooteco.subway.controller.dto.request;

public class LineUpdateRequestForm {

    private String name;
    private String color;
    private Long extraFare = 0L;

    public LineUpdateRequestForm() {
    }

    public LineUpdateRequestForm(String name, String color, Long extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getExtraFare() {
        return extraFare;
    }
}
