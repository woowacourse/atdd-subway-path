package wooteco.subway.dto.request;

public class FindPathRequest {
    private Long source;
    private Long target;
    private int age;

    private FindPathRequest() {
    }

    public FindPathRequest(Long source, Long target, int age) {
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
