package wooteco.subway.admin.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

/**
 *    엣지 클래스입니다.
 *
 *    @author HyungJu An, KeunHwi Choi
 */
public class Edge extends DefaultWeightedEdge {
	private final Long preStationId;
	private final Long stationId;
	private final int distance;
	private final int duration;

	private Edge(final Long preStationId, final Long stationId, final int distance, final int duration) {
		this.preStationId = preStationId;
		this.stationId = stationId;
		this.distance = distance;
		this.duration = duration;
	}

	public static Edge of(LineStation lineStation) {
		return new Edge(lineStation.getPreStationId(), lineStation.getStationId(), lineStation.getDistance(),
			lineStation.getDuration());
	}

	public Long getPreStationId() {
		return preStationId;
	}

	public Long getStationId() {
		return stationId;
	}

	public int getDistance() {
		return distance;
	}

	public int getDuration() {
		return duration;
	}
}
