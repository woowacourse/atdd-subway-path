package wooteco.subway.admin.dto;

import java.util.ArrayList;
import java.util.List;

public class WholeSubwayResponse {

	private List<LineDetailResponse> lineDetails;

	public WholeSubwayResponse() {
	}

	public WholeSubwayResponse(List<LineDetailResponse> lineDetails) {
		this.lineDetails = new ArrayList<>(lineDetails);
	}

	public List<LineDetailResponse> getLineDetails() {
		return lineDetails;
	}
}
