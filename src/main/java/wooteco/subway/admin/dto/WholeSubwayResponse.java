package wooteco.subway.admin.dto;

import java.util.List;

// TODO 구현하세요 :)
public class WholeSubwayResponse {
    private List<LineDetailResponse> lineDetailResponses;

    private WholeSubwayResponse(List<LineDetailResponse> lineDetailResponses) {
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
