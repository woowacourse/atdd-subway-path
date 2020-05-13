package wooteco.subway.admin.dto.response;

import java.util.List;

public class WholeSubwayResponse {
    private List<LineDetailResponse> lineDetailResponse;

    private WholeSubwayResponse() {
    }

    private WholeSubwayResponse(List<LineDetailResponse> lineDetailResponse) {
        this.lineDetailResponse = lineDetailResponse;
    }

    public static WholeSubwayResponse of(List<LineDetailResponse> lineDetailResponse) {
        return new WholeSubwayResponse(lineDetailResponse);
    }

    public List<LineDetailResponse> getLineDetailResponse() {
        return lineDetailResponse;
    }
}
