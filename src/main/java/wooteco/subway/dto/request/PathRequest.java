package wooteco.subway.dto.request;

import javax.validation.constraints.Positive;

public class PathRequest {

    @Positive(message = "출발역의 id는 1보다 작을 수 없습니다.")
    private final Long source;
    @Positive(message = "도착역의 id는 1보다 작을 수 없습니다.")
    private final Long target;
    @Positive(message = "나이는 1보다 작을 수 없습니다.")
    private final int age;

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

}
