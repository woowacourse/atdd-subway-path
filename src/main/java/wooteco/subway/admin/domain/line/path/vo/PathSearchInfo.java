package wooteco.subway.admin.domain.line.path.vo;

public class PathSearchInfo {
    private final String departureStationName;
    private final String arrivalStationName;

    public PathSearchInfo(String departureStationName, String arrivalStationName) {
        validate(departureStationName, arrivalStationName);
        this.departureStationName = departureStationName;
        this.arrivalStationName = arrivalStationName;
    }

    private void validate(String departureStationName, String arrivalStationName) {
        if (departureStationName.equals(arrivalStationName)) {
            throw new IllegalArgumentException("출발역과 도착역은 같을 수 없습니다.");
        }
    }

    public String getDepartureStationName() {
        return departureStationName;
    }

    public String getArrivalStationName() {
        return arrivalStationName;
    }
}
