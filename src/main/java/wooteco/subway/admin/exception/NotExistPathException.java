package wooteco.subway.admin.exception;

public class NotExistPathException extends NotFoundException {
    public NotExistPathException(String source, String target) {
        super(String.format("(%s, %s) 구간이 존재하지 않습니다.", source, target));
    }
}
