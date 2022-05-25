package wooteco.subway.service.dto.request;

public class StationRequest {

    private final String name;

    public StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
