package wooteco.subway.exception;

public class IdNotFoundException extends NotFoundException {

    private static final String NO_ID_MESSAGE = "[ERROR] 해당 ID로 조회할 수 없습니다. error id = ";

    public IdNotFoundException(long id) {
        super(NO_ID_MESSAGE + id);
    }
}
