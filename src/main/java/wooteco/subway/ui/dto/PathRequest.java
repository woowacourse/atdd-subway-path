package wooteco.subway.ui.dto;

import javax.validation.constraints.NotNull;
import wooteco.subway.service.dto.PathServiceRequest;

public class PathRequest {

    @NotNull(message = "출발역Id를 입력해주세요.")
    private Long source;
    @NotNull(message = "도착역Id를 입력해주세요.")
    private Long target;
    @NotNull(message = "나이를 입력해주세요.")
    private int age;

    public PathRequest() {
    }

    public PathRequest(Long source, Long target, int age) {
        this.source = source;
        this.target = target;
        this.age = age;
    }

    public PathServiceRequest toPathServiceRequest() {
        return new PathServiceRequest(source, target, age);
    }
}
