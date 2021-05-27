package wooteco.subway.exception;

public class ExcessiveDistanceArgumentException extends SubwayDomainException {
    public static final String MESSAGE = "기존 구간의 길이를 넘어서는 길이의 구간을 삽입할 수 없습니다.";

    public ExcessiveDistanceArgumentException() {
        super(MESSAGE);
    }
}
