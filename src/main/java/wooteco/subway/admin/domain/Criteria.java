package wooteco.subway.admin.domain;

import java.util.Arrays;
import java.util.function.BiFunction;

import wooteco.subway.admin.exception.NoCriteriaExistsException;

public enum Criteria {
	DISTANCE((duration, distance) -> (double)distance),
	DURATION((duration, distance) -> (double)duration);

	private BiFunction<Integer, Integer, Double> weightSelector;

	Criteria(BiFunction<Integer, Integer, Double> weightSelector) {
		this.weightSelector = weightSelector;
	}

	public static Criteria of(String criteriaType) {
		return Arrays.stream(values())
			.filter(type -> type.name().equals(criteriaType.toUpperCase()))
				.findFirst()
				.orElseThrow(NoCriteriaExistsException::new);
	}

	public double getWeight(int duration, int distance) {
		return weightSelector.apply(duration, distance);
	}
}
