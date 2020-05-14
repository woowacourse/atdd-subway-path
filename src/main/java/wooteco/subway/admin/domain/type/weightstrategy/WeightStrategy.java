package wooteco.subway.admin.domain.type.weightstrategy;

import wooteco.subway.admin.domain.entity.LineStation;

public interface WeightStrategy {
	double getWeight(LineStation lineStation);
}
