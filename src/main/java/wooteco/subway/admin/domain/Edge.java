package wooteco.subway.admin.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class Edge extends DefaultWeightedEdge {
	private int duration;
	private int distance;
	private String criteria;

	public Edge() {
	}

	public Edge(int duration, int distance, String criteria) {
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

	public String getCriteria() {
		return criteria;
	}


}
