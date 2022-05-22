package wooteco.subway.domain.path;

import wooteco.subway.domain.path.factory.PathFactory;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class Path {
    private final List<Section> sections;
    private final List<Station> stations;

    public Path(List<Section> sections, List<Station> stations) {
        this.sections = sections;
        this.stations = stations;
    }

    public static Path of(PathFactory pathFactory, Station source, Station target) {
        return pathFactory.createShortestPath(source, target);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int calculateDistance() {
        return sections.stream()
                .mapToInt(Section::getDistance)
                .sum();
    }

    public int getPathExtraFare(Map<Long, Integer> lineExtraFares) {
        return sections.stream()
                .map(Section::getLineId)
                .mapToInt(lineExtraFares::get)
                .max()
                .orElseThrow(() -> new NoSuchElementException("경로가 존재하지 않습니다."));
    }
}
