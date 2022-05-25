package wooteco.subway.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import wooteco.subway.domain.vo.LineColor;
import wooteco.subway.domain.vo.LineExtraFare;
import wooteco.subway.domain.vo.LineId;
import wooteco.subway.domain.vo.LineName;

public class Line {

    private final LineId id;
    private final LineName name;
    private final LineColor color;
    private final LineExtraFare extraFare;
    private final Sections sections;

    public Line(LineId id, LineName name, LineColor color, LineExtraFare extraFare, Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.sections = sections;
    }

    public Line(LineId id, LineName name, LineColor color, Sections sections) {
        this(id, name, color, LineExtraFare.from(0L), sections);
    }

    public Line(LineId id, LineName name, LineColor color, LineExtraFare extraFare) {
        this(id, name, color, extraFare, null);
    }

    public Line(LineId id, LineName name, LineColor color) {
        this(id, name, color, LineExtraFare.from(0L), null);
    }

    public Line(LineName name, LineColor color, Sections sections) {
        this(null, name, color, LineExtraFare.from(0L), sections);
    }

    public Line(LineName name, LineColor color, LineExtraFare extraFare, Sections sections) {
        this(null, name, color, extraFare, sections);
    }

    public static Line of(Line line, Sections sections) {
        return new Line(LineId.from(line.getId()), LineName.from(line.getName()), LineColor.from(line.getColor()),
                LineExtraFare.from(line.getExtraFare()), sections);
    }

    public List<Station> getStations() {
        return sections.getSections()
                .stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .distinct()
                .collect(Collectors.toList());
    }

    public boolean isEmptyStations() {
        return Objects.isNull(sections);
    }

    public Long getId() {
        return id.getId();
    }

    public String getName() {
        return name.getName();
    }

    public String getColor() {
        return color.getColor();
    }

    public Sections getSections() {
        return sections;
    }

    public Long getExtraFare() {
        return extraFare.getExtraFare();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(name, line.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
