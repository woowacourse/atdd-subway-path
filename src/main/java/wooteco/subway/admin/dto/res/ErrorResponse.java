package wooteco.subway.admin.dto.res;

import java.util.List;

import org.springframework.validation.BindingResult;

import wooteco.subway.admin.exception.ErrorCode;

public class ErrorResponse {
    private String message;

    private int status;

    private List<FieldError> errors;

    public ErrorResponse() {
    }

    public ErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public ErrorResponse(ErrorCode errorCode) {
        this(errorCode.getMessage(), errorCode.getStatus());
    }

    public ErrorResponse(ErrorCode errorCode, BindingResult result) {
        final ErrorResponse errorResponse = new ErrorResponse(errorCode);
        while (result.hasErrors()) {
            errorResponse.addBindingError(result.getFieldError());
        }
    }

    private void addBindingError(org.springframework.validation.FieldError fieldError) {
        errors.add(new FieldError(fieldError.getField(), fieldError.toString(),
            fieldError.getDefaultMessage()));
    }

    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        public FieldError() {
        }

        public FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public String getField() {
            return field;
        }

        public String getValue() {
            return value;
        }

        public String getReason() {
            return reason;
        }
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public List<FieldError> getErrors() {
        return errors;
    }
}
