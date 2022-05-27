package wooteco.subway.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class PathRequest {

    @NotNull(message = "출발역은 빈 값일 수 없습니다.")
    private Long source;

    @NotNull(message = "도착역은 빈 값일 수 없습니다.")
    private Long target;

    @NotNull(message = "나는 빈 값일 수 없습니다.")
    @Min(value = 0, message = "나이는 음수일 수 없습니다.") // 최소값 제한
    @Max(value = 150, message = "나이는 150세를 넘을 수 없습니다.") // 최대값 제한
    private Integer age;

    public PathRequest() {
    }

    public PathRequest(long source, long target, int age) {
        this.source = source;
        this.target = target;
        this.age = age;
    }

    public long getSource() {
        return source;
    }

    public long getTarget() {
        return target;
    }

    public int getAge() {
        return age;
    }

    public void setSource(long source) {
        this.source = source;
    }

    public void setTarget(long target) {
        this.target = target;
    }

    public void setAge(int age) {
        this.age = age;
    }
}


