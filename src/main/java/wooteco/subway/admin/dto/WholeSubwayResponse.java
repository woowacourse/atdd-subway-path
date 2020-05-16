package wooteco.subway.admin.dto;

import java.util.ArrayList;
import java.util.List;

public class WholeSubwayResponse {

	private List<LineResponse> lineDetails;

	public WholeSubwayResponse() {
	}

	public WholeSubwayResponse(List<LineResponse> lineDetails) {
		this.lineDetails = new ArrayList<>(lineDetails);
	}

	public List<LineResponse> getLineDetails() {
		return lineDetails;
	}
}
