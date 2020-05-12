package wooteco.subway.admin.dto;

import java.util.List;

// TODO 구현하세요 :)
public class WholeSubwayResponse {

    private List<LineDetailResponse> lineDetailResponses;

    public WholeSubwayResponse() {
    }

    public WholeSubwayResponse(List<LineDetailResponse> lineDetailResponses) {
        this.lineDetailResponses = lineDetailResponses;
    }

    public static WholeSubwayResponse of(List<LineDetailResponse> lineDetailResponses) {
        return new WholeSubwayResponse(lineDetailResponses);
    }

    public List<LineDetailResponse> getLineDetailResponse() {
        return lineDetailResponses;
    }

    public List<LineDetailResponse> getLineDetailResponses() {
        return lineDetailResponses;
    }

    public void setLineDetailResponses(List<LineDetailResponse> lineDetailResponses) {
        this.lineDetailResponses = lineDetailResponses;
    }
}
