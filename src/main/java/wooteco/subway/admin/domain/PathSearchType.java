package wooteco.subway.admin.domain;

import java.util.Arrays;
import java.util.function.Function;

import wooteco.subway.admin.exception.IllegalPathSearchTypeException;

public enum PathSearchType {
	DISTANCE("DISTANCE", LineStation::getDistance),
	DURATION("DURATION", LineStation::getDuration);

	private final String name;
	private final Function<LineStation, Integer> edgeWeightSelector;

	PathSearchType(String name,
		Function<LineStation, Integer> edgeWeightSelector) {
		this.name = name;
		this.edgeWeightSelector = edgeWeightSelector;
	}

	public int getEdgeWeight(LineStation lineStation) {
		return edgeWeightSelector.apply(lineStation);
	}

	public static PathSearchType of(String typeName) {
		return Arrays.stream(values())
			.filter(type -> type.name.equals(typeName))
			.findFirst()
			.orElseThrow(IllegalPathSearchTypeException::new);
	}
}
