package wooteco.subway.dto;

public class PathFindRequest {

    private final Long source;
    private final Long target;
    private final int age;

    public PathFindRequest(Long source, Long target, int age) {
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
