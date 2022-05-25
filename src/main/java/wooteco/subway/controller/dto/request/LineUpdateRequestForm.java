package wooteco.subway.controller.dto.request;

public class LineUpdateRequestForm {

    private String name;
    private String color;

    public LineUpdateRequestForm() {
    }

    public LineUpdateRequestForm(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
