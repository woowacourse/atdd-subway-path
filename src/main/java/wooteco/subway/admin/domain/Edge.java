package wooteco.subway.admin.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class Edge extends DefaultWeightedEdge {
	private int duration;
	private int distance;
	private Criteria criteria;

	public Edge() {
	}

	public Edge(int duration, int distance, Criteria criteria) {
		this.duration = duration;
		this.distance = distance;
		this.criteria = criteria;
	}

	public int getDuration() {
		return duration;
	}

	public int getDistance() {
		return distance;
	}

	public double getWeight() {
		return criteria.getWeight(duration, distance);
	}


}
