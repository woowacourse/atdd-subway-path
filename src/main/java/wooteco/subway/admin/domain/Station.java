package wooteco.subway.admin.domain;

import java.time.LocalDateTime;

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

	public boolean isSameId(Long stationId) {
		return id.equals(stationId);
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
