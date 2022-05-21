package wooteco.subway.ui;

import java.nio.channels.NonReadableChannelException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.exception.AllStationsInSectionException;
import wooteco.subway.exception.AllStationsNotInSectionException;
import wooteco.subway.exception.DistanceExceedException;
import wooteco.subway.exception.LineDuplicationException;
import wooteco.subway.exception.NonRemovableSectionException;
import wooteco.subway.exception.NotExistLineException;
import wooteco.subway.exception.NotExistStationException;
import wooteco.subway.exception.StationDuplicationException;
import wooteco.subway.exception.StationNotInSectionException;

@ControllerAdvice
public class ErrorControllerAdvice {

    public static final String SERVER_ERROR_MESSAGE = "서버 내부 오류가 발생했습니다.";

    @ExceptionHandler({AllStationsInSectionException.class, AllStationsNotInSectionException.class,
            DistanceExceedException.class, LineDuplicationException.class, NonRemovableSectionException.class,
            NonReadableChannelException.class, NotExistLineException.class, NotExistStationException.class,
            StationDuplicationException.class, StationNotInSectionException.class})
    public ResponseEntity<String> illegalArgumentExceptionHandler(Exception exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle() {
        return ResponseEntity.internalServerError().body(SERVER_ERROR_MESSAGE);
    }

}
