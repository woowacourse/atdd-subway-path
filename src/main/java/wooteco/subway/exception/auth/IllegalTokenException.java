package wooteco.subway.exception.auth;

public class IllegalTokenException extends LoginFailException {

    public IllegalTokenException() {
        super("[ERROR] 유효하지 않은 토큰입니다.");
    }
}
