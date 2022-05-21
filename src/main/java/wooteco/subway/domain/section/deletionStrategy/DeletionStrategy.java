package wooteco.subway.domain.section.deletionStrategy;

import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

import java.util.List;
import java.util.Optional;

public interface DeletionStrategy {
    void delete(List<Section> sections, Long lineId, Station station);

    Optional<Section> fixDisconnectedSection(List<Section> sections, Long lineId, Station station);
}
