package wooteco.subway.domain;

import java.util.Objects;

public class Id {

    private static final long TEMPORARY_ID = 0L;

    private final long id;

    public Id(long id) {
        validateIdPositive(id);
        this.id = id;
    }

    private Id() {
        this.id = TEMPORARY_ID;
    }

    public static Id temporary() {
        return new Id();
    }

    private void validateIdPositive(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("식별자는 양수여야 합니다.");
        }
    }
    
    public boolean isTemporary() {
        return id == TEMPORARY_ID;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Id id1 = (Id) o;
        return id == id1.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
