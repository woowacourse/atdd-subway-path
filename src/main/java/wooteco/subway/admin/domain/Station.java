package wooteco.subway.admin.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

public class Station {
    @Id
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    private Station() {
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

    public boolean isSameName(String stationName) {
        return name.equals(stationName);
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
