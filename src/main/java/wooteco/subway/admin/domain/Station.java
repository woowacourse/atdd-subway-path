package wooteco.subway.admin.domain;

import org.springframework.data.annotation.Id;
import wooteco.subway.admin.service.errors.PathException;

import java.time.LocalDateTime;

public class Station {
    private static final String KOREAN_WORD = "^[가-힣]*$";

    @Id
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    public Station() {
    }

    public Station(String name) {
        this.name = checkKoreanStationName(name);
        this.createdAt = LocalDateTime.now();
    }

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }

    public String checkKoreanStationName(String name) {
        if (name.matches(Station.KOREAN_WORD)) {
            return name;
        }
        throw new PathException("지하철 역은 한국어만 가능합니다");
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
