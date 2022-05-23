package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Path {
    private final List<Station> stations;
    private final List<Long> usedLines;
    private final int distance;

    private Path(List<Station> stations, List<Long> usedLines, int distance) {
        this.stations = stations;
        this.usedLines = usedLines;
        this.distance = distance;
    }

    public static Path from(List<Section> sections, Station departure, Station arrival) {
        ShortestPath shortestPath = ShortestPath.generate(sections, departure, arrival);
        final List<Station> stations = shortestPath.getPath();
        final int distance = shortestPath.getDistance();
        final List<Section> shortestSections = new ArrayList<>();
        for (int i = 0; i < stations.size() - 1; i++) {
            shortestSections.add(findSection(sections, stations, i));
        }

        final Set<Long> lines = shortestSections.stream()
                .map(Section::getLineId)
                .collect(Collectors.toSet());

        return new Path(stations, new ArrayList<>(lines), distance);
    }

    private static Section findSection(List<Section> sections, List<Station> stations, int i) {
        return sections.stream()
                .filter(section -> (section.isEqualToUpStation(stations.get(i)) && section.isEqualToDownStation(
                        stations.get(i + 1))) || ((section.isEqualToDownStation(stations.get(i)) && section
                        .isEqualToUpStation(stations.get(i + 1)))))
                .findFirst()
                .orElseThrow();
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public int getDistance() {
        return distance;
    }

    public List<Long> getUsedLines() {
        return Collections.unmodifiableList(usedLines);
    }
}
