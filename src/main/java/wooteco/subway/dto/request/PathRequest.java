package wooteco.subway.dto.request;

public class PathRequest {

    private Long source;
    private Long target;
    private int age;

    private PathRequest(){}

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
