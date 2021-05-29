package wooteco.subway.exception;

public class NoneOfStationExistException extends SubwayDomainException {
    public static final String MESSAGE = "추가하려는 구간의 상행, 하행역이 기존 노선에 존재하지 않습니다.";

    public NoneOfStationExistException() {
        super(MESSAGE);
    }
}
