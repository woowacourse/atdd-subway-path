package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.List;

import wooteco.subway.domain.fare.DiscountPolicy;
import wooteco.subway.domain.fare.Fare;

public class PathInfo {

	private final List<Station> stations;
	private final int distance;
	private final Fare fare;

	public PathInfo(List<Station> stations, int distance, int extraFare) {
		validateOverZero(distance);
		this.stations = new ArrayList<>(stations);
		this.distance = distance;
		this.fare = Fare.of(distance, extraFare);
	}

	public PathInfo(List<Station> stations, Number distance, Fare extraFare) {
		validateOverZero(distance.intValue());
		this.stations = new ArrayList<>(stations);
		this.distance = distance.intValue();
		this.fare = extraFare;
	}

	private void validateOverZero(int value) {
		if (value < 0) {
			throw new IllegalArgumentException("0 이상이어야 합니다.");
		}
	}

	public PathInfo discountFare(DiscountPolicy discountPolicy) {
		return new PathInfo(stations, distance, fare.discount(discountPolicy));
	}

	public List<Station> getStations() {
		return new ArrayList<>(stations);
	}

	public int getFare() {
		return fare.getValue();
	}

	public int getDistance() {
		return distance;
	}
}
