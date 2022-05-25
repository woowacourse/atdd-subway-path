package wooteco.subway.controller.dto.response;

public class StationResponseForm {

    private Long id;
    private String name;

    public StationResponseForm() {
    }

    public StationResponseForm(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
