package wooteco.subway.service;


import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
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

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse save(StationRequest stationRequest) {
        Station newStation = new Station(stationRequest.getName());
        validateName(newStation);

        Long stationId = stationDao.save(newStation);
        return createStationResponse(stationDao.findById(stationId));
    }

    private void validateName(Station station) {
        if (stationDao.existByName(station)) {
            throw new DuplicateStationException();
        }
    }

    private StationResponse createStationResponse(Station newStation) {
        return new StationResponse(newStation.getId(), newStation.getName());
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findByStationIds(List<Long> stationsId) {
        return stationsId.stream()
            .map(id -> createStationResponse(stationDao.findById(id)))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAll() {
        return stationDao.findAll().stream()
            .map(this::createStationResponse)
            .collect(Collectors.toUnmodifiableList());
    }

    public void delete(Long stationId) {
        stationDao.deleteById(stationId);
    }

    @Transactional(readOnly = true)
    public void validateExistById(Long stationId) {
        if (!stationDao.existById(stationId)) {
            throw new NotFoundStationException();
        }
    }
}
