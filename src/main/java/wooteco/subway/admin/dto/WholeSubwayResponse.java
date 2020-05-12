package wooteco.subway.admin.dto;

import java.util.List;

public class WholeSubwayResponse {
    private List<LineDetailResponse> lineDetailResponses;

    public static WholeSubwayResponse of(List<LineDetailResponse> lineDetailResponse) {
        return new WholeSubwayResponse(lineDetailResponse);
    }

    public WholeSubwayResponse(List<LineDetailResponse> lineDetailResponse) {
        this.lineDetailResponses = lineDetailResponse;
    }

    public WholeSubwayResponse() {
    }

    public List<LineDetailResponse> getLineDetailResponses() {
        return lineDetailResponses;
    }
}
