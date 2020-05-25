package wooteco.subway.admin.domain.type;

import java.util.Arrays;

import wooteco.subway.admin.domain.type.weightstrategy.DistanceWeightStrategy;
import wooteco.subway.admin.domain.type.weightstrategy.DurationWeightStrategy;
import wooteco.subway.admin.domain.type.weightstrategy.WeightStrategy;

public enum WeightType {
	DURATION(new DurationWeightStrategy()),
	DISTANCE(new DistanceWeightStrategy());

	private WeightStrategy weightStrategy;

	WeightType(WeightStrategy weightStrategy) {
		this.weightStrategy = weightStrategy;
	}

	public static WeightType of(String type) {
		WeightType weightType = WeightType.valueOf(type);

		return Arrays.stream(values())
			.filter(it -> it == weightType)
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException(type + ": 찾을 수 없는 타입입니다."));
	}

	public WeightStrategy getWeightStrategy() {
		return weightStrategy;
	}
}
