package wooteco.subway.admin.dto;

import wooteco.subway.admin.domain.type.WeightType;

public class PathRequest {
	private Long sourceId;
	private Long targetId;
	private WeightType weightType;

	public PathRequest() {
	}

	public PathRequest(Long sourceId, Long targetId, WeightType weightType) {
		this.sourceId = sourceId;
		this.targetId = targetId;
		this.weightType = weightType;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public Long getTargetId() {
		return targetId;
	}

	public WeightType getWeightType() {
		return weightType;
	}
}
