package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Path {

	private static final int BASIC_FARE = 1250;
	private static final int INCREASE_FARE = 100;
	private static final int PER_DISTANCE_OVER_TEN = 5;
	private static final int PER_DISTANCE_OVER_FIFTY = 8;

	private final List<Station> stations;
	private final int distance;

	public Path(List<Station> stations, Number distance) {
		this.stations = new ArrayList<>(stations);
		this.distance = distance.intValue();
	}

	public int calculateFare() {
		return BASIC_FARE + getOverTenFare() + getOverFiftyFare();
	}

	private int getOverTenFare() {
		if (distance > 10) {
			return Math.min(800, (int)(Math.ceil((distance - 10) / PER_DISTANCE_OVER_TEN) + 1) * INCREASE_FARE);
		}
		return 0;
	}

	private int getOverFiftyFare() {
		if (distance > 50) {
			return (int)(Math.ceil((distance - 50) / PER_DISTANCE_OVER_FIFTY)) * INCREASE_FARE;
		}
		return 0;
	}

	public List<Station> getStations() {
		return new ArrayList<>(stations);
	}

	public int getDistance() {
		return distance;
	}
}
