package wooteco.subway.admin.domain.line.path;

public class NoPathException extends RuntimeException {
    public NoPathException() {
        super("경로가 존재하지 않습니다.");
    }
}
