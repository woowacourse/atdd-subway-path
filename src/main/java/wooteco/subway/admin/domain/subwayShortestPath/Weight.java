package wooteco.subway.admin.domain.subwayShortestPath;

public class Weight {
	final int distance;
	final int duration;

	public Weight(int distance, int duration) {
		this.distance = distance;
		this.duration = duration;
	}

	public static Weight zero() {
		return new Weight(0, 0);
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
