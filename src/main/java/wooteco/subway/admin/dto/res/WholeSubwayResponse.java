package wooteco.subway.admin.dto.res;

import java.util.List;

public class WholeSubwayResponse {
    private List<LineDetailResponse> responses;

    public WholeSubwayResponse() {
    }

    public WholeSubwayResponse(List<LineDetailResponse> responses) {
        this.responses = responses;
    }

    public static WholeSubwayResponse of(List<LineDetailResponse> responses) {
        return new WholeSubwayResponse(responses);
    }

    public List<LineDetailResponse> getResponses() {
        return responses;
    }
}
