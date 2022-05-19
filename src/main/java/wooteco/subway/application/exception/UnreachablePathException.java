package wooteco.subway.application.exception;

public class UnreachablePathException extends IllegalArgumentException {

    public UnreachablePathException(Long source, Long target) {
        super(String.format("%d에서 %d까지 가는 경로가 없습니다.", source, target));
    }
}
