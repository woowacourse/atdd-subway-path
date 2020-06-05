package wooteco.subway.admin.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

public class Station {
    @Id
    private final Long id;
    private final String name;
    @CreatedDate
    private LocalDateTime createdAt;

    Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Station from(String name) {
        return new Station(null, name);
    }

    public static Station of(Long id, String name) {
        return new Station(id, name);
    }

    public Station withId(Long id) {
        return new Station(id, this.name);
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
