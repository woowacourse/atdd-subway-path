package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.station.StationResponse;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse createStation(String name) {
        validateExistName(name);
        Station station = stationDao.create(new Station(name));

        return new StationResponse(station);
    }

    private void validateExistName(String name) {
        if (stationDao.existByName(name)) {
            throw new IllegalArgumentException("[ERROR] 중복된 이름이 존재합니다.");
        }
    }


    @Transactional(readOnly = true)
    public List<StationResponse> findStations() {
        return stationDao.findAll().stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<Station> findAll() {
        return stationDao.findAll();
    }


    @Transactional(readOnly = true)
    public Station findById(Long id) {
        return stationDao.findById(id);
    }

    public void delete(Long id) {
        validateNonFoundId(id);

        stationDao.deleteById(id);
    }

    private void validateNonFoundId(Long id) {
        if (!stationDao.existById(id)) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 역 입니다.");
        }
    }
}
