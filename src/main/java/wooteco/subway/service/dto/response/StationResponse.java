package wooteco.subway.service.dto.response;


public class StationResponse {

    private long id;
    private String name;

    public StationResponse() {
    }

    public StationResponse(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
