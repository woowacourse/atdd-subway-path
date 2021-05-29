package wooteco.subway.api;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.common.dto.ErrorResponse;
import wooteco.subway.exception.SubwayDomainException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = {LineController.class, StationController.class, PathController.class})
public class SubwayControllerAdvice {
    @ExceptionHandler(SubwayDomainException.class)
    public ResponseEntity<ErrorResponse> handleSubwayDomainException(SubwayDomainException e) {
        e.printStackTrace();
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }
}
