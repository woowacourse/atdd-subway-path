package wooteco.subway.domain.line;

import java.util.List;
import java.util.Objects;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.domain.station.Station;

public class LineMap {

    private final Line line;
    private final Sections sections;

    public LineMap(Line line, Sections sections) {
        this.line = line;
        this.sections = sections;
    }

    public static LineMap of(Line line, Section section) {
        return new LineMap(line, new Sections(section));
    }

    public Long getId() {
        return line.getId();
    }

    public String getName() {
        return line.getName();
    }

    public String getColor() {
        return line.getColor();
    }

    public int getExtraFare() {
        return  line.getExtraFare();
    }

    public Sections getSections() {
        return sections;
    }

    public List<Station> getSortedStations() {
        return sections.toSortedStations();
    }

    public List<Section> toSectionList() {
        return sections.toSortedList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineMap lineMap = (LineMap) o;
        return Objects.equals(line, lineMap.line)
                && Objects.equals(sections, lineMap.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, sections);
    }

    @Override
    public String toString() {
        return "LineMap{" + "lineInfo=" + line + ", sections=" + sections + '}';
    }
}
