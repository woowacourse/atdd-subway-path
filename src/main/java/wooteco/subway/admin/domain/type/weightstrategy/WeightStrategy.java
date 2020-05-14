package wooteco.subway.admin.domain.type.weightstrategy;

import wooteco.subway.admin.domain.LineStation;

public interface WeightStrategy {
	double getWeight(LineStation lineStation);
}
