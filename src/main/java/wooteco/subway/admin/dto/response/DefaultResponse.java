package wooteco.subway.admin.dto.response;

import org.apache.logging.log4j.util.Strings;

public class DefaultResponse<T> {
    private T data;
    private String message;

    private DefaultResponse() {
    }

    public DefaultResponse(T data, String message) {
        this.data = data;
        this.message = message;
    }

    public static <T> DefaultResponse<T> of(T data) {
        return new DefaultResponse<>(data, Strings.EMPTY);
    }

    public static <Void> DefaultResponse<Void> error(String message) {
        return new DefaultResponse<>(null, message);
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
