package wooteco.subway.admin.dto.response;

import wooteco.subway.admin.domain.LineDetail;

import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class WholeSubwayResponse {
    private List<LineDetailResponse> lineDetailResponses;

    public WholeSubwayResponse() {
    }

    public WholeSubwayResponse(List<LineDetailResponse> lineDetailResponses) {
        this.lineDetailResponses = lineDetailResponses;
    }

    public static WholeSubwayResponse of(List<LineDetail> lineDetails) {
        return lineDetails.stream()
                .map(LineDetailResponse::of)
                .collect(collectingAndThen(toList(), WholeSubwayResponse::new));
    }

    public List<LineDetailResponse> getLineDetailResponses() {
        return lineDetailResponses;
    }
}
