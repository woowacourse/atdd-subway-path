package wooteco.subway.admin.dto;

import wooteco.subway.admin.domain.line.vo.PathInfo;

public class PathRequest {
    private final String departureStationName;
    private final String arrivalStationName;

    public PathRequest(String departureStationName, String arrivalStationName) {
        this.departureStationName = departureStationName;
        this.arrivalStationName = arrivalStationName;
    }

    public String getDepartureStationName() {
        return departureStationName;
    }

    public String getArrivalStationName() {
        return arrivalStationName;
    }

    public PathInfo toPathInfo() {
        return new PathInfo(departureStationName, arrivalStationName);
    }
}
