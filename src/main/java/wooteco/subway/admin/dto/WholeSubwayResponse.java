package wooteco.subway.admin.dto;

import java.util.List;
import java.util.Objects;

public class WholeSubwayResponse {
    private List<LineDetailResponse> lineDetailResponses;

    private WholeSubwayResponse() {
    }

    public WholeSubwayResponse(List<LineDetailResponse> lineDetailResponses) {
        this.lineDetailResponses = lineDetailResponses;
    }

    public static WholeSubwayResponse of(List<LineDetailResponse> responses) {
        return new WholeSubwayResponse(responses);
    }

    public List<LineDetailResponse> getLineDetailResponses() {
        return lineDetailResponses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        WholeSubwayResponse that = (WholeSubwayResponse)o;
        return Objects.equals(lineDetailResponses, that.lineDetailResponses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineDetailResponses);
    }
}
