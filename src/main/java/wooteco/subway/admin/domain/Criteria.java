package wooteco.subway.admin.domain;

import wooteco.subway.admin.exception.NoCriteriaExistsException;

import java.util.Arrays;
import java.util.function.BiFunction;

public enum Criteria {
	DISTANCE("distance", ((duration, distance) -> (double) distance)),
	DURATION("duration", ((duration, distance) -> (double) duration));

	private String criteriaType;
	private BiFunction<Integer, Integer, Double> weightSelector;

	Criteria(String criteriaType, BiFunction<Integer, Integer, Double> weightSelector) {
		this.criteriaType = criteriaType;
		this.weightSelector = weightSelector;
	}

	public static Criteria of(String criteriaType) {
		return Arrays.stream(values())
				.filter(type -> type.criteriaType.equals(criteriaType))
				.findFirst()
				.orElseThrow(NoCriteriaExistsException::new);
	}

	public double getWeight(int duration, int distance) {
		return weightSelector.apply(duration, distance);
	}
}
