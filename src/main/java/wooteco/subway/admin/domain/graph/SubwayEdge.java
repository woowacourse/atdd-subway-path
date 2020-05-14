package wooteco.subway.admin.domain.graph;

import org.jgrapht.graph.DefaultWeightedEdge;

import wooteco.subway.admin.domain.entity.LineStation;
import wooteco.subway.admin.domain.type.weightstrategy.WeightStrategy;

public class SubwayEdge extends DefaultWeightedEdge {
	private final LineStation lineStation;
	private final WeightStrategy weightStrategy;

	public SubwayEdge(LineStation lineStation, WeightStrategy weightStrategy) {
		super();
		this.lineStation = lineStation;
		this.weightStrategy = weightStrategy;
	}

	@Override
	protected double getWeight() {
		return weightStrategy.getWeight(lineStation);
	}

	public int getDuration() {
		return lineStation.getDuration();
	}

	public int getDistance() {
		return lineStation.getDistance();
	}
}
