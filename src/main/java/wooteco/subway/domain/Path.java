package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.List;

import wooteco.subway.domain.fare.Fare;

public class Path {

	private final List<Station> stations;
	private final int distance;
	private final Fare fare;

	public Path(List<Station> stations, int distance, int extraFare) {
		validateOverZero(distance);
		this.stations = new ArrayList<>(stations);
		this.distance = distance;
		this.fare = Fare.of(distance, extraFare);
	}

	public Path(List<Station> stations, Number distance, Fare extraFare) {
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

	public Path disCountByAge(int age) {
		return new Path(stations, distance, fare.discountByAge(age));
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
