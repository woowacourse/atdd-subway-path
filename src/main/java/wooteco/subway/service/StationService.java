package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.exception.DataNotFoundException;
import wooteco.subway.exception.DuplicateNameException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse createStation(final StationRequest stationRequest) {
        final Station rawStation = stationRequest.toEntity();
        validateDuplicateName(rawStation);
        final Station station = stationDao.save(rawStation);

        return StationResponse.from(station);
    }

    public List<StationResponse> getAllStations() {
        final List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    public void delete(final Long id) {
        final int affectedRows = stationDao.deleteById(id);

        if (affectedRows == 0) {
            throw new DataNotFoundException("존재하지 않는 노선 id 입니다.");
        }
    }

    private void validateDuplicateName(final Station station) {
        if (stationDao.existByName(station.getName())) {
            throw new DuplicateNameException("이미 존재하는 지하철 역입니다.");
        }
    }
}
