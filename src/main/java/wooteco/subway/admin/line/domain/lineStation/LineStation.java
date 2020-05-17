package wooteco.subway.admin.line.domain.lineStation;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.lang.Nullable;

public class LineStation {

	@Id
	@Column("id")
	private Long id;

	@Column("index")
	private Long index;

	@Nullable
	@Column("pre_station_id")
	private Long preStationId;

	@Column("station_id")
	private Long stationId;

	@Column("distance")
	private Integer distance;

	@Column("duration")
	private Integer duration;

	public LineStation() {
	}

	public LineStation(Long preStationId, Long stationId, Integer distance, Integer duration) {
		this.index = null;
		this.preStationId = preStationId;
		this.stationId = stationId;
		this.distance = distance;
		this.duration = duration;
	}

	public LineStation(Long index, Long preStationId, Long stationId, Integer distance, Integer duration) {
		this.index = index;
		this.preStationId = preStationId;
		this.stationId = stationId;
		this.distance = distance;
		this.duration = duration;
	}

	public Long getIndex() {
		return index;
	}

	public Long getPreStationId() {
		return preStationId;
	}

	public Long getStationId() {
		return stationId;
	}

	public Integer getDistance() {
		return distance;
	}

	public Integer getDuration() {
		return duration;
	}

	public void updatePreLineStation(Long preStationId) {
		this.preStationId = preStationId;
	}

	public boolean isSamePreStation(Long preStationId) {
		return preStationId.equals(this.preStationId);
	}

	public boolean isSameStation(Long stationId) {
		return stationId.equals(this.stationId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		LineStation that = (LineStation)o;
		return Objects.equals(id, that.id) &&
			Objects.equals(index, that.index) &&
			Objects.equals(preStationId, that.preStationId) &&
			Objects.equals(stationId, that.stationId) &&
			Objects.equals(distance, that.distance) &&
			Objects.equals(duration, that.duration);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, index, preStationId, stationId, distance, duration);
	}

}
