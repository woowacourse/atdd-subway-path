package wooteco.subway.domain.station;

import java.util.List;

public interface StationRepository {

    Station save(Station station);

    List<Station> getAll();

    Station getById(long stationId);

    void remove(long stationId);

    boolean existsById(long stationID);

    boolean existsByName(String name);
}
