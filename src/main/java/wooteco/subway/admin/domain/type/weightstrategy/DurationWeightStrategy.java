package wooteco.subway.admin.domain.type.weightstrategy;

import wooteco.subway.admin.domain.entity.LineStation;

public class DurationWeightStrategy implements WeightStrategy {
	@Override
	public double getWeight(LineStation lineStation) {
		return lineStation.getDuration();
	}
}
