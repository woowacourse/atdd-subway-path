package wooteco.subway.api;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.common.dto.ErrorResponse;

import java.sql.SQLException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = {LineController.class, StationController.class, PathController.class})
public class SubwayControllerAdvice {
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorResponse> handleSQLException(SQLException e) {
        e.printStackTrace();
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }
}
