package wooteco.subway.service;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.StationRequest;
import wooteco.subway.dto.response.StationResponse;
import wooteco.subway.exception.DuplicateStationException;
import wooteco.subway.exception.NotFoundStationException;

@Service
@Transactional
public class StationService {

    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public StationResponse save(final StationRequest stationRequest) {
        final Station newStation = new Station(stationRequest.getName());
        validateName(newStation);

        final Long stationId = stationDao.save(newStation);
        return createStationResponse(stationDao.findById(stationId));
    }

    private void validateName(final Station station) {
        if (stationDao.existByName(station)) {
            throw new DuplicateStationException("이미 존재하는 역 이름입니다.");
        }
    }

    private StationResponse createStationResponse(final Station newStation) {
        return new StationResponse(newStation.getId(), newStation.getName());
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<StationResponse> findByStationIds(final List<Long> stationIds) {
        final Map<Long, Station> stations = stationDao.findAll()
                .stream()
                .filter(station -> stationIds.contains(station.getId()))
                .collect(Collectors.toMap(Station::getId, station -> station));

        return stationIds.stream()
                .map(stationId -> createStationResponse(stations.get(stationId)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<StationResponse> findAll() {
        return stationDao.findAll()
                .stream()
                .map(this::createStationResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    public void delete(final Long stationId) {
        stationDao.deleteById(stationId);
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public void validateExistById(final Long stationId) {
        if (!stationDao.existById(stationId)) {
            throw new NotFoundStationException("존재하지 않는 지하철 역입니다.");
        }
    }
}
