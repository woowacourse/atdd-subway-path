package wooteco.subway.ui;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.AddSectionException;
import wooteco.subway.exception.CalculatePathsException;
import wooteco.subway.exception.ClientException;
import wooteco.subway.exception.DeleteSectionException;
import wooteco.subway.exception.datanotfound.DataNotFoundException;
import wooteco.subway.exception.duplicatename.DuplicateNameException;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({DuplicateNameException.class, CalculatePathsException.class,
            AddSectionException.class, DeleteSectionException.class})
    public ResponseEntity<String> handleDuplicateNameException(ClientException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler({DataNotFoundException.class})
    public ResponseEntity<String> handleDataNotFoundException(ClientException exception) {
        return ResponseEntity.internalServerError().body(exception.getMessage());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> handleUnexpectedException(Exception exception) {
        log.error(exception.getMessage());
        return ResponseEntity.internalServerError().body("예상치 못한 에러가 발생했습니다.");
    }
}
