package wooteco.subway.admin.domain;

import java.util.Objects;

public class LineStationKey {
	private final Long preStationId;
	private final Long stationId;

	public LineStationKey(Long preStationId, Long stationId) {
		this.preStationId = preStationId;
		this.stationId = stationId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LineStationKey that = (LineStationKey) o;
		return Objects.equals(preStationId, that.preStationId) &&
				Objects.equals(stationId, that.stationId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(preStationId, stationId);
	}
}
