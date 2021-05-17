package wooteco.exception.notfound;

public class StationNotFoundException extends NotFoundException {

    private static final String MESSAGE = "존재하지 않는 역을 입력하였습니다.";

    public StationNotFoundException() {
        super(MESSAGE);
    }
}
