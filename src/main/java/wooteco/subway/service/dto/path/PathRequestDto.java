package wooteco.subway.service.dto.path;

public class PathRequestDto {

    private final Long source;
    private final Long target;
    private final int age;

    public PathRequestDto(Long source, Long target, int age) {
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
