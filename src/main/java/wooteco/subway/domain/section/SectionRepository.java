package wooteco.subway.domain.section;

import java.util.List;
import wooteco.subway.domain.station.Station;

public interface SectionRepository {

    List<Section> findSections();

    Station findStationById(Long stationId);
}
