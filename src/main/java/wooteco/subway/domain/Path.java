package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Path {

	private final List<Station> stations;
	private final int distance;
	private final Fare fare;

	public Path(List<Station> stations, Number distance, Fare fare) {
		validateOverZero(distance.intValue());
		this.stations = new ArrayList<>(stations);
		this.distance = distance.intValue();
		this.fare = fare;
	}

	private void validateOverZero(int value) {
		if (value < 0) {
			throw new IllegalArgumentException("0 이상이어야 합니다.");
		}
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
