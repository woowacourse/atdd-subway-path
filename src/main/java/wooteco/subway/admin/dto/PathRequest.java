package wooteco.subway.admin.dto;

import javax.validation.constraints.NotBlank;

public class PathRequest {
	@NotBlank(message = "출발역을 입력해주세요.")
	private String source;
	@NotBlank(message = "도착역을 입력해주세요.")
	private String target;
	@NotBlank(message = "최단경로 검색 타입을 입력해주세요.")
	private String type;

	public PathRequest(String source, String target, String type) {
		this.source = source;
		this.target = target;
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public String getTarget() {
		return target;
	}

	public String getType() {
		return type;
	}
}
