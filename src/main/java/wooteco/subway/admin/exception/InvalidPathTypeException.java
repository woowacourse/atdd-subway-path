package wooteco.subway.admin.exception;

public class InvalidPathTypeException extends RuntimeException{

    public InvalidPathTypeException() {
        super("적절하지 않은 타입 입력입니다");
    }
}
