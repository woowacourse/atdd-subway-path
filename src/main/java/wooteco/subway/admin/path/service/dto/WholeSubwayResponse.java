package wooteco.subway.admin.path.service.dto;

import java.util.ArrayList;
import java.util.List;

import wooteco.subway.admin.line.service.dto.line.LineDetailResponse;

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
