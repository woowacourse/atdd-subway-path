package wooteco.subway.dto;

import java.util.List;

public class ErrorResponse {

    private final List<String> messages;

    public ErrorResponse(final List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }
}
