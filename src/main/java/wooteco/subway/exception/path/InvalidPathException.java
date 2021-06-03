package wooteco.subway.exception.path;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;
import wooteco.subway.station.domain.Station;

public class InvalidPathException extends SubwayException {
    public InvalidPathException(Station source, Station target) {
        super(HttpStatus.BAD_REQUEST, "source : " + source.getName() + ", target : " + target.getName() + " 경로 찾기 에러");
    }
}
