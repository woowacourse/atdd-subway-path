package wooteco.subway.admin.exception;

public class StationNotFoundException extends NotFoundException {
    public StationNotFoundException(Long id) {
        super(String.format("id가 %d인 역이 존재하지 않습니다.", id));
    }

    public StationNotFoundException(String name) {
        super(String.format("이름이 %s인 역이 존재하지 않습니다.", name));
    }
}
