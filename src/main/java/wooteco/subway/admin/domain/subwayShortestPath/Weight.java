package wooteco.subway.admin.domain.subwayShortestPath;

public class Weight {
	private static final int ZERO = 0;

	private final int distance;
	private final int duration;

	public Weight(int distance, int duration) {
		this.distance = distance;
		this.duration = duration;
	}

	public static Weight zero() {
		return new Weight(ZERO, ZERO);
	}

	public Weight addWeight(int distance, int duration) {
		return new Weight(this.distance + distance, this.duration + duration);
	}

	public int getDistance() {
		return distance;
	}

	public int getDuration() {
		return duration;
	}
}
