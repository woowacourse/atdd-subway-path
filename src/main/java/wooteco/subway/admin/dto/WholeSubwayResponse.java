package wooteco.subway.admin.dto;

import java.util.List;
import java.util.UUID;

public class WholeSubwayResponse {
    private List<LineDetailResponse> lineDetailResponses;

    public WholeSubwayResponse(List<LineDetailResponse> lineDetailResponses) {
        this.lineDetailResponses = lineDetailResponses;
    }

    public WholeSubwayResponse() {
    }

    public static WholeSubwayResponse of(List<LineDetailResponse> responses) {
        return new WholeSubwayResponse(responses);
    }

    public List<LineDetailResponse> getLineDetailResponses() {
        return lineDetailResponses;
    }
}
