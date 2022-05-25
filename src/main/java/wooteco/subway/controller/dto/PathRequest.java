package wooteco.subway.controller.dto;

import javax.validation.constraints.NotNull;

public class PathRequest {

	@NotNull
	private final Long source;
	@NotNull
	private final Long target;
	@NotNull
	private final int age;

	public PathRequest(Long source, Long target, int age) {
		this.source = source;
		this.target = target;
		this.age = age;
	}

	public Long getSource() {
		return source;
	}

	public Long getTarget() {
		return target;
	}

	public int getAge() {
		return age;
	}
}
