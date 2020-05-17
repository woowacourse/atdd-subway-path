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

    public Station(final String name) {
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }

    public Station(final Long id, final String name) {
        this.id = id;
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }

    public boolean is(Station other) {
        return id.equals(other.id);
    }

    public boolean is(Long stationId) {
        return id.equals(stationId);
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
}
