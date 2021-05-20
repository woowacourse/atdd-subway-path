package wooteco.subway.auth.dto;

public class Payload {

    private final String payload;

    public Payload(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }
}
