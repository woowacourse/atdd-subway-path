package wooteco.subway.admin.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

public abstract class CreateTime {

    @CreatedDate
    private LocalDateTime createdAt;

    public CreateTime() {
    }

    public CreateTime(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
