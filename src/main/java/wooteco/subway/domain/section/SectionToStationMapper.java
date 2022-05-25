package wooteco.subway.domain.section;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import wooteco.subway.domain.station.Station;
import wooteco.subway.exception.UnexpectedException;

public class SectionToStationMapper {

    public List<Station> toStation(List<Section> sections) {
        final Map<Station, Station> sectionMap = toMap(sections);
        final Station upTerminal = findUpTerminal(sectionMap);
        return getOrderedStations(sectionMap, upTerminal);
    }

    private Map<Station, Station> toMap(List<Section> sections) {
        return sections.stream()
            .collect(Collectors.toMap(
                Section::getUpStation,
                Section::getDownStation));
    }

    private Station findUpTerminal(Map<Station, Station> sectionMap) {
        return sectionMap.keySet()
            .stream()
            .filter(upStation -> !sectionMap.containsValue(upStation))
            .findAny()
            .orElseThrow(() -> new UnexpectedException("상행 종점을 찾을 수 없습니다."));
    }

    private List<Station> getOrderedStations(Map<Station, Station> sectionMap, Station cursor) {
        List<Station> orderedStations = new ArrayList<>();
        while (cursor != null) {
            orderedStations.add(cursor);
            cursor = sectionMap.get(cursor);
        }
        return orderedStations;
    }
}
