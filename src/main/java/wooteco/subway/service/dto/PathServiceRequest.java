package wooteco.subway.service.dto;

public class PathServiceRequest {

    private final long departureId;
    private final long arrivalId;
    private final int age;

    public PathServiceRequest(long departureId, long arrivalId, int age) {
        this.departureId = departureId;
        this.arrivalId = arrivalId;
        this.age = age;
    }

    public long getDepartureId() {
        return departureId;
    }

    public long getArrivalId() {
        return arrivalId;
    }

    public int getAge() {
        return age;
    }
}
