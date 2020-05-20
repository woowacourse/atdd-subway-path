package wooteco.subway.admin.domain;

public enum EdgeWeightType {
	DISTANCE,
	DURATION;

	public static EdgeWeightType of(String type) {
		return EdgeWeightType.valueOf(type);
	}
}
