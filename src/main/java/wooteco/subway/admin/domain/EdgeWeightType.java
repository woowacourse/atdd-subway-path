package wooteco.subway.admin.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum EdgeWeightType {
	DISTANCE(lineStation -> Double.valueOf(lineStation.getDistance())),
	DURATION(lineStation -> Double.valueOf(lineStation.getDuration()));

	private Function<LineStation, Double> getWeightType;

	EdgeWeightType(
		Function<LineStation, Double> getWeightType) {
		this.getWeightType = getWeightType;
	}

	public static EdgeWeightType of(String type) {
		return Arrays.stream(values())
			.filter(value -> value.name().equals(type.toUpperCase()))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("잘못된 EdgeWeight"));
	}

	public double getWeight(LineStation lineStation) {
		return getWeightType.apply(lineStation);
	}
}
