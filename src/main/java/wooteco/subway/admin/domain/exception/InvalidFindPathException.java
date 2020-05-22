package wooteco.subway.admin.domain.exception;

public class InvalidFindPathException extends IllegalArgumentException {

    public static final String DUPLICATE_SOURCE_WITH_TARGET_ERROR_MSG = "출발역과 도착역은 동일할 수 없습니다.";
    public static final String NO_PATH_ERROR_MSG = "경로를 찾을 수 없습니다. 노선도를 확인해주세요.";

    public InvalidFindPathException(String msg) {
        super(msg);
    }
}
