package wooteco.subway.admin.domain;

import java.util.function.Function;

public enum EdgeWeightType {
	DISTANCE(lineStation -> Long.valueOf(lineStation.getDistance())),
	DURATION(lineStation -> Long.valueOf(lineStation.getDuration()));

	private Function<LineStation, Long> weight;

	EdgeWeightType(Function<LineStation, Long> weight) {
		this.weight = weight;
	}

	public Long getWeight(LineStation lineStation) {
		return weight.apply(lineStation);
	}
}
