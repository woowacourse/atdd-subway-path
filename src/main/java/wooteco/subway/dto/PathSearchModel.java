package wooteco.subway.dto;

public class PathSearchModel {

    private Long source;
    private Long target;
    private int age;

    public PathSearchModel(Long source, Long target, int age) {
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
