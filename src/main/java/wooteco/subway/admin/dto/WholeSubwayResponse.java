package wooteco.subway.admin.dto;

import java.util.List;

public class WholeSubwayResponse {
    private List<LineDetailResponse> lineDetailResponses;

    public WholeSubwayResponse(List<LineDetailResponse> lineDetailResponses) {
        this.lineDetailResponses = lineDetailResponses;
    }

    public static WholeSubwayResponse of(List<LineDetailResponse> lineDetailResponses) {
        return new WholeSubwayResponse(lineDetailResponses);
    }

    public List<LineDetailResponse> getLineDetailResponses() {
        return lineDetailResponses;
    }
}
