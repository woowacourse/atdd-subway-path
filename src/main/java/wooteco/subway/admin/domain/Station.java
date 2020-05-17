package wooteco.subway.admin.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.data.annotation.Id;

public class Station {
	@Id
	private final Long id;
	private final String name;
	private final LocalDateTime createdAt;

	Station(final Long id, final String name, final LocalDateTime createdAt) {
		this.id = id;
		this.name = name;
		this.createdAt = createdAt;
	}

	public static Station of(final String name) {
		return new Station(null, name, LocalDateTime.now());
	}

	public Station withId(Long id) {
		return new Station(id, this.name, this.createdAt);
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Station station = (Station)o;
		return Objects.equals(id, station.id) &&
			Objects.equals(name, station.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}
}
