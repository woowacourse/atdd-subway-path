package wooteco.subway.ui.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import wooteco.subway.service.dto.LineServiceRequest;
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

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public int getAge() {
        return age;
    }

    public PathServiceRequest toPathServiceRequest() {
        return new PathServiceRequest(source, target, age);
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public void setTarget(Long target) {
        this.target = target;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
