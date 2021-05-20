package wooteco.subway.member.ui;

import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.member.NotRegisteredMemberException;

@RestControllerAdvice
public class MemberControllerAdvice {

    @ExceptionHandler(NotRegisteredMemberException.class)
    public ResponseEntity<String> notRegisterMemberException(NotRegisteredMemberException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(e.getMessage());
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<String> malformedJwtException(MalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(e.getMessage());
    }
}
