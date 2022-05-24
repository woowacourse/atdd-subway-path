package wooteco.subway.dto.exceptionMessage;

public class ExceptionMessageDto {
    private final String message;

    public ExceptionMessageDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
