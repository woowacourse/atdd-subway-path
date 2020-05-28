package wooteco.subway.admin.dto.response;

import org.apache.logging.log4j.util.Strings;

public class StandardResponse<T> {
    private T data;
    private String message;

    private StandardResponse() {
    }

    public StandardResponse(T data, String message) {
        this.data = data;
        this.message = message;
    }

    public static <T> StandardResponse<T> of(T data) {
        return new StandardResponse<>(data, Strings.EMPTY);
    }

    public static <Void> StandardResponse<Void> error(String message) {
        return new StandardResponse<>(null, message);
    }

    public static <Void> StandardResponse<Void> empty() {
        return new StandardResponse<>(null, Strings.EMPTY);
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
