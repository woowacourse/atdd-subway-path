package wooteco.subway.dto.request;

import javax.validation.constraints.Min;

public class PathRequest {

    private static final String NUMBER_MIN_RANGE_ERROR = " 1 이상이여야 합니다.";

    @Min(value = 1, message = "시작역 아이디는" + NUMBER_MIN_RANGE_ERROR)
    private Long source;

    @Min(value = 1, message = "도착점 아이디는" + NUMBER_MIN_RANGE_ERROR)
    private Long target;

    @Min(value = 1, message = "나이는 " + NUMBER_MIN_RANGE_ERROR)
    private int age;

    private PathRequest(){}

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
}
