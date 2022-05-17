package wooteco.subway.dto;

public class PathRequest {

    private Long source;
    private Long target;
    private int age;

    public PathRequest(Long source, Long target, int age) {
        this.source = source;
        this.target = target;
        this.age = age;
    }

    @Override
    public String toString() {
        return "PathRequest{" +
                "source=" + source +
                ", target=" + target +
                ", age=" + age +
                '}';
    }
}
