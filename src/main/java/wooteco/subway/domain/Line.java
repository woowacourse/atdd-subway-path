package wooteco.subway.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import wooteco.subway.exception.constant.BlankArgumentException;

import java.util.Objects;

import static wooteco.subway.utils.StringUtils.isBlank;

@Getter
@EqualsAndHashCode
@ToString
public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final Integer extraFare;

    public Line(Long id) {
        this(id, null, null, 0);
    }

    public Line(String name, String color, Integer extraFare) {
        this(null, name, color, extraFare);
    }

    public Line(Long id, String name, String color, Integer extraFare) {
        if (isBlank(name) || isBlank(color)) {
            throw new BlankArgumentException();
        }
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public boolean isSameName(String name) {
        return this.name.equals(name);
    }
}
