package wooteco.subway.admin.dto;

import javax.validation.constraints.NotBlank;

import wooteco.subway.admin.domain.PathSearchType;

public class PathRequest {
	@NotBlank(message = "출발역을 입력해주세요.")
	private String source;
	@NotBlank(message = "도착역을 입력해주세요.")
	private String target;
	private PathSearchType type;

	public String getSource() {
		return source;
	}

	public String getTarget() {
		return target;
	}

	public PathSearchType getType() {
		return type;
	}

	public PathRequest(String source, String target, PathSearchType type) {
		this.source = source;
		this.target = target;
		this.type = type;
	}
}
