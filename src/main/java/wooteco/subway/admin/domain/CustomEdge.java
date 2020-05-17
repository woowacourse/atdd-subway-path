package wooteco.subway.admin.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class CustomEdge extends DefaultWeightedEdge {
	private LineStation lineStation;
	private EdgeWeightType type;

	public CustomEdge(LineStation lineStation, EdgeWeightType type) {
		this.lineStation = lineStation;
		this.type = type;
	}

	@Override
	public double getWeight() {
		return type.getWeight(lineStation);
	}

	public int getDistance() {
		return lineStation.getDistance();
	}

	public int getDuration() {
		return lineStation.getDuration();
	}
}
