package wooteco.subway.line.domain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import wooteco.subway.exception.BusinessRelatedException;
import wooteco.subway.station.domain.Station;

public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public Set<Station> toDistinctStations() {
        return lines.stream()
            .flatMap(Line::toStationStream)
            .collect(Collectors.toSet());
    }

    public Set<Section> toAllSections() {
        return lines.stream()
            .flatMap(Line::toSectionStream)
            .collect(Collectors.toSet());
    }

    public Line findLineBySectionContaining(Section section) {
        return lines.stream()
            .filter(line -> line.contains(section))
            .findAny()
            .orElseThrow(
                () -> new BusinessRelatedException(
                    String.format("전체 노선에 해당 구간이 없습니다. (상행: %s, 하행: %s)",
                        section.getUpStation(), section.getDownStationName())
                )
            );
    }

    public Stream<Line> toStream() {
        return lines.stream();
    }
}
