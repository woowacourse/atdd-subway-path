package wooteco.subway.admin.exception;

public class IllegalTypeNameException extends BusinessException {

    public IllegalTypeNameException(String typeName) {
        super(typeName +"방식의 경로는 지원하지 않습니다.");
    }
}
