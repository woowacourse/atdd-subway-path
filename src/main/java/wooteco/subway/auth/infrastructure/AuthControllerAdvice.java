package wooteco.subway.auth.infrastructure;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"wooteco.subway.auth"})
public class AuthControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e)  {
        return ResponseEntity.badRequest().body(e.getAllErrors().get(0).getDefaultMessage());
    }
}
