package wooteco.subway.domain.station;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Station {
    private Long id;
    private final String name;

    public Station(String name) {
        this(null, name);
    }

    public boolean isSameId(Long id) {
        return id.equals(this.id);
    }
}
