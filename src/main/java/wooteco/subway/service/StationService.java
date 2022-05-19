package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {

    private static final String STATION_DUPLICATION = "이미 등록된 지하철 역입니다.";
    private static final String STATION_NOT_EXIST = "존재하지 않은 지하철역입니다.";
    private static final int STATION_EXIST_VALUE = 1;
    private static final int DELETE_SUCCESS = 1;

    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Transactional
    public StationResponse save(final StationRequest stationRequest) {
        final Station station = new Station(stationRequest.getName());
        validateDuplication(station);
        final Station newStation = stationDao.save(station);
        return new StationResponse(newStation.getId(), newStation.getName());
    }

    private void validateDuplication(Station station) {
        int existFlag = stationDao.isExistStation(station);
        if (existFlag == STATION_EXIST_VALUE) {
            throw new IllegalArgumentException(STATION_DUPLICATION);
        }
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAll() {
        final List<Station> stations = stationDao.findAll();
        return stations.stream()
                .map(it -> new StationResponse(it.getId(), it.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(final Long id) {
        if (stationDao.deleteById(id) != DELETE_SUCCESS) {
            throw new IllegalArgumentException(STATION_NOT_EXIST);
        }
    }
}
