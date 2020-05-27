package wooteco.subway.admin.domain.type;

import java.util.Arrays;

import wooteco.subway.admin.domain.type.weightstrategy.DistanceWeightStrategy;
import wooteco.subway.admin.domain.type.weightstrategy.DurationWeightStrategy;
import wooteco.subway.admin.domain.type.weightstrategy.WeightStrategy;

public enum WeightType {
	DURATION("DURATION", new DurationWeightStrategy()),
	DISTANCE("DISTANCE", new DistanceWeightStrategy());

	private String weightType;
	private WeightStrategy weightStrategy;

	WeightType(String weightType, WeightStrategy weightStrategy) {
		this.weightType = weightType;
		this.weightStrategy = weightStrategy;
	}

	public static WeightType of(String type) {
		return Arrays.stream(values())
			.filter(it -> it.weightType.equals(type))
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException(type + ": 찾을 수 없는 타입입니다."));
	}

	public WeightStrategy getWeightStrategy() {
		return weightStrategy;
	}
}
