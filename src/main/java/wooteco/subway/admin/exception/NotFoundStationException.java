package wooteco.subway.admin.exception;

import org.springframework.dao.DataAccessException;

public class NotFoundStationException extends DataAccessException {

    public NotFoundStationException(String name) {
        super(name + "역을 찾을 수 없습니다.");
    }
}
