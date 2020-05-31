package wooteco.subway.admin.domain;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

public class Line {
	@Id
	private Long id;
	private String name;
	private LocalTime startTime;
	private LocalTime endTime;
	private int intervalTime;
	private String bgColor;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	@Embedded.Empty
	private LineStations lineStations;

	public Line() {
	}

	public Line(Long id, String name, LocalTime startTime, LocalTime endTime, int intervalTime, String bgColor) {
		this.id = id;
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.intervalTime = intervalTime;
		this.bgColor = bgColor;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
		this.lineStations = new LineStations();
	}

	public Line(String name, LocalTime startTime, LocalTime endTime, int intervalTime) {
		this(null, name, startTime, endTime, intervalTime, "bg-red-400");
	}

	public void update(Line line) {
		if (line.getName() != null) {
			this.name = line.getName();
		}
		if (line.getStartTime() != null) {
			this.startTime = line.getStartTime();
		}
		if (line.getEndTime() != null) {
			this.endTime = line.getEndTime();
		}
		if (line.getIntervalTime() != 0) {
			this.intervalTime = line.getIntervalTime();
		}

		this.updatedAt = LocalDateTime.now();
	}

	public void addLineStation(LineStation lineStation) {
		lineStations.addLineStation(lineStation);
	}

	public void removeLineStationById(Long stationId) {
		lineStations.removeLineStationById(stationId);
	}

	public void setUpGraph(WeightedMultigraph<Long, CustomEdge> graph, EdgeWeightType type) {
		lineStations.setUpGraph(graph, type);
	}

	public List<Long> getLineStationsId() {
		return lineStations.getLineStationsId();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public int getIntervalTime() {
		return intervalTime;
	}

	public LineStations getLineStations() {
		return lineStations;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public String getBgColor() {
		return this.bgColor;
	}
}
