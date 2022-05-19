package wooteco.subway.domain.station;

import java.util.List;

public interface StationRepository {

    Station save(Station station);

    List<Station> findAll();

    Station findStationById(Long stationId);

    void deleteById(Long stationId);
}
