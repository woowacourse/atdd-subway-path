package wooteco.subway.dto;

import java.util.List;

public class WholeSubwayResponse {
	private List<LineDetailResponse> lineDetailResponse;

	WholeSubwayResponse() {}

	private WholeSubwayResponse(List<LineDetailResponse> lineDetailResponse) {
		this.lineDetailResponse = lineDetailResponse;
	}

	public static WholeSubwayResponse of(List<LineDetailResponse> responses) {
		return new WholeSubwayResponse(responses);
	}

	public List<LineDetailResponse> getLineDetailResponse() {
		return lineDetailResponse;
	}
}
