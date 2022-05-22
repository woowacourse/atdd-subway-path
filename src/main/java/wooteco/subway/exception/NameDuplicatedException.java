package wooteco.subway.exception;

public class NameDuplicatedException extends SubwayException {

    private static final String NAME_DUPLICATE_MESSAGE = "[ERROR] 이미 존재하는 이름입니다. error name = ";

    public NameDuplicatedException(String name) {
        super(NAME_DUPLICATE_MESSAGE + name);
    }
}
