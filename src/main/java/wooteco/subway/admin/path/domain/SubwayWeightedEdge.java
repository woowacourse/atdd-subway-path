package wooteco.subway.admin.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SubwayWeightedEdge extends DefaultWeightedEdge {

	private double subWeight;

	public double getSubWeight() {
		return subWeight;
	}

	public void setSubWeight(int subWeight) {
		this.subWeight = subWeight;
	}
}
