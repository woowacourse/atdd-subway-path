package wooteco.subway.dto.request;

public class PathRequest {
    private Long source;
    private Long target;
    private Long age;

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
