package wooteco.subway.ui.dto;

import static wooteco.subway.ui.dto.LineCreationRequest.OMISSION_MESSAGE;

import javax.validation.constraints.NotNull;
import wooteco.subway.service.dto.PathServiceRequest;

public class PathRequest {

    @NotNull(message = "출발역" + OMISSION_MESSAGE)
    private Long source;

    @NotNull(message = "도착역" + OMISSION_MESSAGE)
    private Long target;

    @NotNull(message = "나이" + OMISSION_MESSAGE)
    private Integer age;

    public PathRequest(Long source, Long target, Integer age) {
        this.source = source;
        this.target = target;
        this.age = age;
    }

    public PathServiceRequest toServiceRequest() {
        return new PathServiceRequest(source, target, age);
    }
}
