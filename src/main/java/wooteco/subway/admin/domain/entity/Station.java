package wooteco.subway.admin.domain.entity;

import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.data.annotation.Id;

public class Station {
	@Id
	private Long id;
	private String name;
	private LocalDateTime createdAt;

	public Station() {
	}

	public Station(String name) {
		this.name = name;
		this.createdAt = LocalDateTime.now();
	}

	public Station(Long id, String name) {
		this.id = id;
		this.name = name;
		this.createdAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public static Station findStationByName(String targetName, Map<Long, Station> stations) {
		return stations.values().stream()
			.filter(station -> station.isSameName(targetName))
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException(
				String.format("%s은 존재하지 않는 역입니다.", targetName)));
	}

	public boolean isSameName(String sourceName) {
		return this.name.equals(sourceName);
	}

	@Override
	public String toString() {
		return "Station{" +
			"id=" + id +
			", name='" + name + '\'' +
			", createdAt=" + createdAt +
			'}';
	}
}
