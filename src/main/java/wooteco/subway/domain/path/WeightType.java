package wooteco.subway.domain.path;

import static wooteco.subway.exception.InvalidPathException.NOT_EXIST_PATH_WEIGHT;

import java.util.Arrays;

import wooteco.subway.exception.InvalidPathException;

public enum WeightType {
	DURATION(StationWeightEdge::getDuration),
	DISTANCE(StationWeightEdge::getDistance);

	private final WeightStrategy strategy;

	WeightType(WeightStrategy strategy) {
		this.strategy = strategy;
	}

	public static WeightStrategy findStrategy(String type) {
		return Arrays.stream(values())
			.filter(weightType -> weightType.isEquals(type))
			.findFirst()
			.orElseThrow(() -> new InvalidPathException(NOT_EXIST_PATH_WEIGHT))
			.strategy;
	}

	private boolean isEquals(String type) {
		return this.name().equals(type);
	}
}
