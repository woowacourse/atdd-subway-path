package wooteco.subway.admin.dto;

import java.util.List;

// TODO 구현하세요 :)
public class WholeSubwayResponse {
    private List<LineDetailResponse> responses;

    private WholeSubwayResponse() {
    }

    public WholeSubwayResponse(final List<LineDetailResponse> responses) {
        this.responses = responses;
    }

    public static WholeSubwayResponse of(List<LineDetailResponse> responses) {
        return new WholeSubwayResponse(responses);
    }

    public List<LineDetailResponse> getResponses() {
        return responses;
    }
}
