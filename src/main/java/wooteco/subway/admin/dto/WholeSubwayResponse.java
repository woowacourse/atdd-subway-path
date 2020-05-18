package wooteco.subway.admin.dto;

import java.util.List;

public class WholeSubwayResponse {
    private List<LineWithStationsResponse> lineWithStationsResponse;

    public static WholeSubwayResponse of(List<LineWithStationsResponse> responses) {
        return new WholeSubwayResponse(responses);
    }

    public WholeSubwayResponse() {
    }

    public WholeSubwayResponse(List<LineWithStationsResponse> lineWithStationsResponse) {
        this.lineWithStationsResponse = lineWithStationsResponse;
    }

    public List<LineWithStationsResponse> getLineWithStationsResponse() {
        return lineWithStationsResponse;
    }
}
