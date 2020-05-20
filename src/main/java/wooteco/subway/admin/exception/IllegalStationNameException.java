package wooteco.subway.admin.exception;

public class IllegalStationNameException extends BusinessException {

    public IllegalStationNameException(Long sourceId, Long targetId) {
        super(sourceId + ", " + targetId + " 역이 중복되었습니다.");
    }
}
