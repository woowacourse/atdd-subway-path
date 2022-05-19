package wooteco.subway.domain.exception;

public class DuplicatedStationsException extends ExpectedException {

    private static final String MESSAGE = "구간 등록시 지하철 역들은 중복될 수 없습니다.";

    public DuplicatedStationsException() {
        super(MESSAGE);
    }
}
