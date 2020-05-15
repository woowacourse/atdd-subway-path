package wooteco.subway.admin.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.LastModifiedDate;

public abstract class UpdateTime extends CreateTime {

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public UpdateTime() {
    }

    public UpdateTime(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UpdateTime(LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt);
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
