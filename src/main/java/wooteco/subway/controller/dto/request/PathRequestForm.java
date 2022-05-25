package wooteco.subway.controller.dto.request;

public class PathRequestForm {

    private Long source;
    private Long target;
    private Long age;

    public PathRequestForm(Long source, Long target, Long age) {
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
