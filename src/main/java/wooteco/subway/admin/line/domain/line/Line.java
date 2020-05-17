package wooteco.subway.admin.line.domain.line;

import static java.util.stream.Collectors.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;

import wooteco.subway.admin.common.exception.SubwayException;
import wooteco.subway.admin.line.domain.lineStation.LineStation;

public class Line {

	@Id
	@Column("id")
	private Long id;

	@Column("name")
	private String name;

	@Column("color")
	private String color;

	@Column("start_time")
	private LocalTime startTime;

	@Column("end_time")
	private LocalTime endTime;

	@Column("interval_time")
	private Integer intervalTime;

	@Column("created_at")
	@CreatedDate
	private LocalDateTime createdAt;

	@Column("updated_at")
	@LastModifiedDate
	private LocalDateTime updatedAt;

	@MappedCollection(idColumn = "line_id", keyColumn = "index")
	private LinkedList<LineStation> stations = new LinkedList<>();

	public Line() {
	}

	public Line(Long id, String name, String color, LocalTime startTime, LocalTime endTime, Integer intervalTime) {
		this.id = id;
		this.name = name;
		this.color = color;
		this.startTime = startTime;
		this.endTime = endTime;
		this.intervalTime = intervalTime;
	}

	public Line(String name, String color, LocalTime startTime, LocalTime endTime, Integer intervalTime) {
		this(null, name, color, startTime, endTime, intervalTime);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public Integer getIntervalTime() {
		return intervalTime;
	}

	public List<LineStation> getStations() {
		return stations;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void update(Line line) {
		if (Objects.nonNull(line.getName())) {
			this.name = line.getName();
		}
		if (Objects.nonNull(line.getStartTime())) {
			this.startTime = line.getStartTime();
		}
		if (Objects.nonNull(line.getEndTime())) {
			this.endTime = line.getEndTime();
		}
		if (Objects.nonNull(line.getIntervalTime())) {
			this.intervalTime = line.getIntervalTime();
		}
		if (Objects.nonNull(line.getColor())) {
			this.color = line.getColor();
		}
		this.updatedAt = LocalDateTime.now();
	}

	public void addLineStation(LineStation lineStation) {
		final Long preStationId = lineStation.getPreStationId();

		if (stations.isEmpty()) {
			checkHeadStationByPreStationId(preStationId);
			stations.add(lineStation);
			return;
		}

		checkLineStationAlreadyExist(lineStation);

		if (Objects.isNull(preStationId)) {
			final LineStation headLineStation = stations.getFirst();
			headLineStation.updatePreLineStation(lineStation.getStationId());
			stations.addFirst(lineStation);
			return;
		}

		final Optional<LineStation> nextLineStation = findLineStationByPreStationId(preStationId);

		if (nextLineStation.isPresent()) {
			nextLineStation.get().updatePreLineStation(preStationId);
			stations.add(stations.indexOf(nextLineStation.get()), lineStation);
			return;
		}
		stations.add(lineStation);
	}
	
	private void checkHeadStationByPreStationId(Long preStationId) {
		if (Objects.nonNull(preStationId)) {
			throw new SubwayException("출발역이 존재하지 않습니다.");
		}
	}

	private void checkLineStationAlreadyExist(LineStation lineStation) {
		final Optional<LineStation> foundLineStation =
			findLineStationByStationId(lineStation.getStationId());

		if (foundLineStation.isPresent()) {
			throw new SubwayException("이미 존재하는 역입니다.");
		}
	}

	private Optional<LineStation> findLineStationByStationId(Long stationId) {
		return stations.stream()
			.filter(lineStation -> lineStation.isSameStation(stationId))
			.findFirst();
	}

	private Optional<LineStation> findLineStationByPreStationId(Long preStationId) {
		return stations.stream()
			.filter(lineStation -> lineStation.isSamePreStation(preStationId))
			.findFirst();
	}

	public void removeLineStationById(Long stationId) {
		final LineStation targetLineStation = findLineStationByStationId(stationId)
			.orElseThrow(() -> new SubwayException("삭제하려는 역이 존재하지 않습니다."));

		stations.remove(targetLineStation);
		findLineStationByPreStationId(stationId)
			.ifPresent(value -> value.updatePreLineStation(targetLineStation.getPreStationId()));
	}

	public List<Long> getLineStationsId() {
		return stations.stream()
			.map(LineStation::getStationId)
			.collect(toList());
	}
}
