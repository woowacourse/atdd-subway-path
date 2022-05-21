package wooteco.subway.exception;

public class NonRemovableSectionException extends IllegalStateException{

    public NonRemovableSectionException(String message) {
        super(message);
    }
}
