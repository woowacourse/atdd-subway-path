package wooteco.subway.admin.dto;

import java.util.List;
import java.util.Map;

public class WholeSubwayResponse {
    private List<LineDetailResponse> lineDetailResponse;

    public static WholeSubwayResponse of(List<LineDetailResponse> lineDetailResponses) {
        return new WholeSubwayResponse(lineDetailResponses);
    }

    public WholeSubwayResponse() {
    }

    public WholeSubwayResponse(List<LineDetailResponse> lineDetailResponse) {
        this.lineDetailResponse = lineDetailResponse;
    }

    public List<LineDetailResponse> getLineDetailResponse() {
        return lineDetailResponse;
    }
}
