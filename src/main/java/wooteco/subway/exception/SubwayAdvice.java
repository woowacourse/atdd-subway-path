package wooteco.subway.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SubwayAdvice {

    @ExceptionHandler(RuntimeException.class)
    public void handle(RuntimeException e){
        System.out.println(e.getMessage());
    }

}
