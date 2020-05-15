package wooteco.subway.admin.domain;

import java.util.Arrays;

public enum EdgeWeightType {
	DISTANCE,
	DURATION;

	public static EdgeWeightType of(String type) {
		return Arrays.stream(values())
			.filter(value -> value.name().equals(type.toUpperCase()))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("잘못된 EdgeWeight"));
	}
}
