package wooteco.subway.ui.dto;

import static wooteco.subway.ui.dto.LineCreationRequest.OMISSION_MESSAGE;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import wooteco.subway.service.dto.PathServiceRequest;

public class PathRequest {

    @NotNull(message = "출발역" + OMISSION_MESSAGE)
    @Min(value = 1, message = "도착역" + OMISSION_MESSAGE)
    private Long source;
    @NotNull(message = "도착역" + OMISSION_MESSAGE)
    @Min(value = 1, message = "도착역" + OMISSION_MESSAGE)
    private Long target;
    @NotNull(message = "나이" + OMISSION_MESSAGE)
    @Min(value = 1, message = "나이" + OMISSION_MESSAGE)
    private Integer age;

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public Integer getAge() {
        return age;
    }

    public PathServiceRequest toServiceRequest() {
        return new PathServiceRequest(source, target, age);
    }
}
