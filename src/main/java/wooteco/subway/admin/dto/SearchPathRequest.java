package wooteco.subway.admin.dto;

public class SearchPathRequest {
    private String startStationName;
    private String targetStationName;
    private String type;

    public SearchPathRequest() {
    }

    public SearchPathRequest(String startStationName, String targetStationName, String type) {
        this.startStationName = startStationName;
        this.targetStationName = targetStationName;
        this.type = type;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public String getTargetStationName() {
        return targetStationName;
    }

    public String getType() {
        return type;
    }
}
