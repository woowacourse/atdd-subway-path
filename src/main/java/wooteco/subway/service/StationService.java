package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.StationRequest;
import wooteco.subway.dto.response.StationResponse;
import wooteco.subway.exception.NotFoundStationException;

@Transactional
@Service
public class StationService {

    private static final String NOT_FOUND_STATION_ERROR_MESSAGE = "해당하는 역이 존재하지 않습니다.";
    private static final String DUPLICATE_STATION_ERROR_MESSAGE = "같은 이름의 역이 존재합니다.";

    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        final Station station = new Station(stationRequest.getName());
        if (stationDao.hasStation(stationRequest.getName())) {
            throw new IllegalArgumentException(DUPLICATE_STATION_ERROR_MESSAGE);
        }
        final Station savedStation = stationDao.save(station);
        return StationResponse.of(savedStation);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStations() {
        final List<Station> stations = stationDao.findAll();
        return stations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional(readOnly = true)
    public StationResponse findStation(Long id) {
        final Station station = stationDao.findById(id)
                .orElseThrow(() -> new NotFoundStationException(NOT_FOUND_STATION_ERROR_MESSAGE));
        return new StationResponse(station.getId(), station.getName());
    }

    public void deleteStation(Long id) {
        checkNotFoundStation(id);
        stationDao.deleteById(id);
    }

    private void checkNotFoundStation(Long id) {
        final Station station = stationDao.findById(id)
                .orElseThrow(() -> new NotFoundStationException(NOT_FOUND_STATION_ERROR_MESSAGE));
        if (station == null) {
            throw new NotFoundStationException(NOT_FOUND_STATION_ERROR_MESSAGE);
        }
    }
}
