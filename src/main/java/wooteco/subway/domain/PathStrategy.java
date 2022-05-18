package wooteco.subway.domain;

import java.util.List;

public interface PathStrategy {

    List<Station> findPath(List<Station> stations, List<Section> sections, Station from, Station to);

    List<Section> findSections(List<Station> stations, List<Section> sections, Station from, Station to);

    int calculateDistance(List<Station> stations, List<Section> sections, Station from, Station to);

    int calculateFee(List<Station> stations, List<Section> sections, Station from, Station to);
}
