package wooteco.subway.admin.line.domain.lineStation;

import java.util.Objects;

import org.springframework.data.annotation.Id;

public class LineStation {

	@Id
	private Long id;
	private Long preStationId;
	private Long stationId;
	private int distance;
	private int duration;

	public LineStation() {
	}

	public LineStation(Long preStationId, Long stationId, int distance, int duration) {
		this.preStationId = preStationId;
		this.stationId = stationId;
		this.distance = distance;
		this.duration = duration;
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

	public void updatePreLineStation(Long preStationId) {
		this.preStationId = preStationId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		LineStation that = (LineStation)o;
		return distance == that.distance &&
			duration == that.duration &&
			Objects.equals(id, that.id) &&
			Objects.equals(preStationId, that.preStationId) &&
			Objects.equals(stationId, that.stationId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, preStationId, stationId, distance, duration);
	}
}
