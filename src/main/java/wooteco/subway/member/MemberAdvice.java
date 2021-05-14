package wooteco.subway.member;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.member.exception.InvalidMemberException;
import wooteco.subway.member.exception.InvalidTokenException;

@RestControllerAdvice("wooteco.subway.member")
public class MemberAdvice {

    @ExceptionHandler(Exception.class)
    private ResponseEntity<Void> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(InvalidMemberException.class)
    private ResponseEntity<Void> handlerInvalidMemberException(InvalidMemberException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(InvalidTokenException.class)
    private ResponseEntity<Void> handleInvalidTokenException(InvalidTokenException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
