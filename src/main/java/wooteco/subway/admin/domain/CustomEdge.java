package wooteco.subway.admin.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class CustomEdge extends DefaultWeightedEdge {
	private int distance;
	private int duration;
	private EdgeWeightType type;

	public CustomEdge(int distance, int duration) {
		this.distance = distance;
		this.duration = duration;
	}

	public CustomEdge(LineStation lineStation, EdgeWeightType type) {
		this.distance = lineStation.getDistance();
		this.duration = lineStation.getDuration();
		this.type = type;
	}

	@Override
	public double getWeight() {
		if (type == EdgeWeightType.DURATION) {
			return duration;
		}
		return distance;
	}

	public int getDistance() {
		return distance;
	}

	public int getDuration() {
		return duration;
	}

	@Override
	public String toString() {
		return "(" + distance + " , " + duration + ")";
	}
}
