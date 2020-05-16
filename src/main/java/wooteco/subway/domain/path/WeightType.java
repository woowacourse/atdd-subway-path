package wooteco.subway.domain.path;

import static wooteco.subway.exception.InvalidPathException.NOT_EXIST_PATH_WEIGHT;

import java.util.Arrays;

import wooteco.subway.exception.InvalidPathException;

public enum WeightType {
	DURATION("DURATION", StationWeightEdge::getDuration),
	DISTANCE("DISTANCE", StationWeightEdge::getDistance);

	private final String name;
	private final WeightStrategy strategy;

	WeightType(String name, WeightStrategy strategy) {
		this.name = name;
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
		return this.name.equals(type);
	}

	public String getName() {
		return name;
	}
}
