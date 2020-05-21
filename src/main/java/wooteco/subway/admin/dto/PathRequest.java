package wooteco.subway.admin.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import wooteco.subway.admin.domain.PathType;

/**
 *    class description
 *
 *    @author HyungJu An
 */
public class PathRequest {
	@NotBlank
	private final String source;
	@NotBlank
	private final String target;
	@NotNull
	private final PathType type;

	public PathRequest(final String source, final String target, final PathType type) {
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

	public PathType getType() {
		return type;
	}
}
