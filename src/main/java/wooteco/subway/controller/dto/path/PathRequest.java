package wooteco.subway.controller.dto.path;

import wooteco.subway.service.dto.path.PathRequestDto;

import javax.validation.constraints.Positive;

public class PathRequest {

    @Positive(message = "[ERROR] 출발 ID는 양수입니다.")
    private Long source;
    @Positive(message = "[ERROR] 도착 ID는 양수입니다.")
    private Long target;
    @Positive(message = "[ERROR] 나이는 양수입니다.")
    private int age;

    private PathRequest() {
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

    public PathRequestDto toServiceRequest() {
        return new PathRequestDto(source, target, age);
    }
}
