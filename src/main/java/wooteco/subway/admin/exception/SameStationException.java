package wooteco.subway.admin.exception;

import org.springframework.validation.BindingResult;

public class SameStationException extends RuntimeException {
    BindingResult bindingResult;

    public SameStationException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
