package wooteco.subway.exception;

public class InvalidPathException extends IllegalArgumentException {

    public InvalidPathException(String source, String target) {
        super(String.format("경로를 찾을 수 없습니다. sourceName: %s target: %s", source, target));
    }
}
