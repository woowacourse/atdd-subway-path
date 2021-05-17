package wooteco.subway.exception.auth;

public class IllegalTokenException extends LoginFailException{

    public IllegalTokenException() {
        super("[ERROR] 토큰의 유효기간이 지났습니다.");
    }
}
