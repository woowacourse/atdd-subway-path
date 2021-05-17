package wooteco.subway.exception;

public class NotExistingPathException extends RuntimeException{

    public NotExistingPathException() {
        super("[ERROR] 존재하는 경로가 없습니다.");
    }
}
