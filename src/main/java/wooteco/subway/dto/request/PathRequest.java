package wooteco.subway.dto.request;

public class PathRequest {

    private final Long sourceId;
    private final Long targetId;
    private final int age;

    public PathRequest() {
        this(null, null, 0);
    }

    public PathRequest(Long sourceId, Long targetId, int age) {
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.age = age;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public int getAge() {
        return age;
    }

}
