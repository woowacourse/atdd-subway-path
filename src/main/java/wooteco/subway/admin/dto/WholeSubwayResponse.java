package wooteco.subway.admin.dto;

import java.util.List;

public class WholeSubwayResponse {
    List<LineDetailResponse> lineDetailResponses;

    private WholeSubwayResponse(List<LineDetailResponse> lineDetailResponses) {
        this.lineDetailResponses = lineDetailResponses;
    }

    public WholeSubwayResponse() {
    }

    public static WholeSubwayResponse of(List<LineDetailResponse> lineDetailResponses) {
        return new WholeSubwayResponse(lineDetailResponses);
    }

    public List<LineDetailResponse> getLineDetailResponses() {
        return lineDetailResponses;
    }
}
