package wooteco.subway.service.dto;

public class PathServiceRequest {

    private final Long departureId;
    private final Long arrivalId;
    private final Integer age;

    public PathServiceRequest(Long departureId, Long arrivalId, Integer age) {
        this.departureId = departureId;
        this.arrivalId = arrivalId;
        this.age = age;
    }

    public Long getDepartureId() {
        return departureId;
    }

    public Long getArrivalId() {
        return arrivalId;
    }

    public Integer getAge() {
        return age;
    }
}
