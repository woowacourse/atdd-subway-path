package wooteco.subway.controller.dto.request;

public class StationRequestForm {

    private String name;

    public StationRequestForm() {
    }

    public StationRequestForm(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
