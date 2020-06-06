package wooteco.subway.admin.domain.type;

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

	public WeightStrategy getWeightStrategy() {
		return weightStrategy;
	}
}
