package wooteco.subway.service.dto;

import javax.validation.constraints.Min;
import wooteco.subway.dto.request.PathRequest;

public class PathDto {

    private Long source;
    private Long target;
    private int age;

    private PathDto(){}

    public PathDto(Long source, Long target, int age) {
        this.source = source;
        this.target = target;
        this.age = age;
    }

    public static PathDto from(final PathRequest request) {
        return new PathDto(request.getSource(), request.getTarget(), request.getAge());
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
}
