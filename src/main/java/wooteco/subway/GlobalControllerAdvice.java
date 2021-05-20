package wooteco.subway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.auth.application.AuthorizedException;
import wooteco.subway.member.application.MemberException;

@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleUnhandledException(Exception e) {
        final String message = "Unhandled exception";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

    @ExceptionHandler(AuthorizedException.class)
    public ResponseEntity<Void> handleInvalidAuthorizationException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<String> handleInvalidMemberException(MemberException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
