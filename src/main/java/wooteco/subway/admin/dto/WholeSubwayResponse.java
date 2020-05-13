package wooteco.subway.admin.dto;

import java.util.List;

public class WholeSubwayResponse {
    private List<LineDetailResponse> lineDetailResponse;
    private Long version = 1L;

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

    public Long getVersion() {
        return version;
    }
}
