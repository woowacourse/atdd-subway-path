package wooteco.exception.conflict;

public class DuplicateException extends ConflictException {

    public DuplicateException() {
        super("중복된 정보가 존재합니다.");
    }
}
