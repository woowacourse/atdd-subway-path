package wooteco.subway.dto;

import java.util.List;

public class ExceptionMessagesDto {
    private final List<String> messages;

    public ExceptionMessagesDto(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }
}
