package wooteco.subway.controller.dto.path;

public class PathRequest {

    private Long source;
    private Long target;
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
