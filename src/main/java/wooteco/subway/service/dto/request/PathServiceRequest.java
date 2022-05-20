package wooteco.subway.service.dto.request;

public class PathServiceRequest {

    private final int source;
    private final int target;
    private final int age;

    public PathServiceRequest(int source, int target, int age) {
        this.source = source;
        this.target = target;
        this.age = age;
    }

    public int getSource() {
        return source;
    }

    public int getTarget() {
        return target;
    }

    public int getAge() {
        return age;
    }
}
