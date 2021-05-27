package wooteco.subway.path.exception;

public class NotFoundStationException extends RuntimeException {

    public NotFoundStationException(Long id) {
        System.out.printf("존재하지 않는 역 ID입니다. ID = %d", id);
    }
}
