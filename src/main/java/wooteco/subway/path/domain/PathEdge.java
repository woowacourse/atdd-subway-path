package wooteco.subway.path.domain;

import java.util.Objects;
import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.exception.ValidationFailureException;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

public class PathEdge extends DefaultWeightedEdge {

    private final Section section;
    private final String lineName;
    private final String color;

    public PathEdge(final Section section, final Line line) {
        validate(section, line);
        this.section = section;
        this.lineName = line.getName();
        this.color = line.getColor();
    }

    private void validate(Section section, Line line) {
        if (!line.contains(section)) {
            throw new ValidationFailureException(
                String.format(
                    "노선에 해당 구간이 없습니다. (노선: %s 상행: %s 하행: %s)",
                    line.getName(), section.getUpStationName(), section.getDownStationName()
                )
            );
        }
    }

    @Override
    protected Station getSource() {
        return section.getUpStation();
    }

    @Override
    protected Station getTarget() {
        return section.getDownStation();
    }

    @Override
    public double getWeight() {
        return section.getDistance();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PathEdge pathEdge = (PathEdge) o;
        return Objects.equals(section, pathEdge.section) && Objects.equals(lineName, pathEdge.lineName)
            && Objects.equals(color, pathEdge.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(section, lineName, color);
    }
}
