package wooteco.subway.dto;

public class PathRequest {

    private final Long source;
    private final Long target;
    private final Long age;

    public PathRequest(Long source, Long target, Long age) {
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

    public Long getAge() {
        return age;
    }
}
