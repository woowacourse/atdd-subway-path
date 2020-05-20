package wooteco.subway.admin.exception;

public enum ErrorCode {
    INVALID_REQUEST(400, "적절하지 못한 입력입니다."),
    SOURCE_TARGET_SAME(400, "동일역으로는 이동할 수 없습니다."),
    NOT_EXIST_STATION(400, "존재하지 않는 역입니다."),
    NO_EDGE(400, "이어지지 않은 역입니다.(이동할 수 없습니다.)"),
    INVALID_REQUEST_METHOD(405, "잘못된 요청입니다.(GET,POST,PUT,DELETE)를 확인해주세요.");

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
