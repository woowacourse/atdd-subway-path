package wooteco.subway.admin.dto;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;

public class ErrorResponse {
    public static final int HTTP_STATUS_BAD_REQUEST = 400;

    private String message;
    private int status;
    private List<FieldError> errors;

    public ErrorResponse() {
    }

    public ErrorResponse(String message) {
        this.message = message;
        this.status = HTTP_STATUS_BAD_REQUEST;
        this.errors = Collections.EMPTY_LIST;
    }

    public ErrorResponse(final ErrorCode errorCode, final List<FieldError> fieldErrors) {
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
        this.errors = fieldErrors;
    }

    public ErrorResponse(String message, int status, List<FieldError> errors) {
        this.message = message;
        this.status = status;
        this.errors = errors;
    }

    public static ErrorResponse of(final ErrorCode errorCode, final BindingResult bindingResult) {
        return new ErrorResponse(errorCode, FieldError.of(bindingResult));
    }

    public static ErrorResponse of(String message) {
        return new ErrorResponse(message);
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

    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        public FieldError() {
        }

        public FieldError(final String field) {
            this.field = field;
        }

        private FieldError(final String field, final String value, final String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();

            return fieldErrors.stream()
                .map(fieldError -> new FieldError(
                    fieldError.getField(),
                    getRightRejectValue(fieldError.getRejectedValue()),
                    fieldError.getDefaultMessage()
                ))
                .collect(Collectors.toList())
                ;
        }

        private static String getRightRejectValue(Object rejectValue) {
            if (Objects.isNull(rejectValue)) {
                return "";
            }
            return rejectValue.toString();
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
}
