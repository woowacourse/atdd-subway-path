package wooteco.subway.exception.path;

public class NotReachableException extends PathException {

    public NotReachableException(Long sourceStationId, Long targetStationId) {
        super(Long.toString(sourceStationId) + "와 " + Long.toString(targetStationId)
            + "는 환승으로 갈 수 없습니다.");
    }
}
