package wooteco.subway.admin.exception;

public class IllegalStationNameException extends BusinessException {
    public IllegalStationNameException(String sourceName) {
        super(sourceName + " 이름이 중복되었습니다.");
    }
}
