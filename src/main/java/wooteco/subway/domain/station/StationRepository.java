package wooteco.subway.domain.station;

import java.util.List;

public interface StationRepository {
    Station save(Station station);

    List<Station> findAll();

    Station find(long id);

    void delete(long id);
}
