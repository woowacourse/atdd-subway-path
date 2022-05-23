package wooteco.subway.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import wooteco.subway.exception.constant.BlankArgumentException;

import java.util.Objects;

import static wooteco.subway.utils.StringUtils.isBlank;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Station {

    private Long id;
    private String name;

    private Station() {
    }

    public Station(Long id) {
        this(id, null);
    }

    public Station(String name) {
        this(null, name);
    }

    public Station(Long id, String name) {
        if (isBlank(name)) {
            throw new BlankArgumentException();
        }
        this.id = id;
        this.name = name;
    }
}
