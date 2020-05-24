package wooteco.subway.admin.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SubwayEdge extends DefaultWeightedEdge {
	private final int duration;
	private final int distance;

	private SubwayEdge(int duration, int distance) {
		this.duration = duration;
		this.distance = distance;
	}

	public static SubwayEdge from(LineStation lineStation) {
		return new SubwayEdge(lineStation.getDuration(), lineStation.getDistance());
	}

	public int getDuration() {
		return duration;
	}

	public int getDistance() {
		return distance;
	}
}
