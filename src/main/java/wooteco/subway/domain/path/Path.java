package wooteco.subway.domain.path;

import java.util.List;

import wooteco.subway.domain.property.Distance;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.SectionToStationMapper;
import wooteco.subway.domain.station.Station;

public class Path {

    private static final SectionToStationMapper STATION_MAPPER = new SectionToStationMapper();

    private final List<Station> stations;
    private final Distance distance;

    public Path(List<Station> stations, Distance distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static Path of(PathFinder finder) {
        final List<Section> sections = finder.getSections();
        return new Path(STATION_MAPPER.toStation(sections), sumDistance(sections));
    }

    private static Distance sumDistance(List<Section> sections) {
        return new Distance(
            sections.stream()
                .mapToInt(section -> section.getDistance().getValue())
                .sum()
        );
    }

    public List<Station> getStations() {
        return List.copyOf(stations);
    }

    public Distance getDistance() {
        return distance;
    }
}
