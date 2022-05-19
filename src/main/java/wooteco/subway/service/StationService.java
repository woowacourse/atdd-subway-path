package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.exception.duplicatename.StationDuplicateException;

@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse createStation(StationRequest stationRequest) {
        validateDuplicateName(stationRequest.getName());
        Station station = new Station(stationRequest.getName());
        Station newStation = save(station);
        return new StationResponse(newStation.getId(), newStation.getName());
    }

    private void validateDuplicateName(String name) {
        if (stationDao.existByName(name)) {
            throw new StationDuplicateException("이미 등록된 지하철역 이름입니다.");
        }
    }

    private Station save(Station station) {
        stationDao.deleteByExistName(station.getName());
        return stationDao.save(station);
    }

    public List<StationResponse> findAll() {
        return stationDao.findAll().stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }

    public int deleteStation(Long id) {
        Station station = stationDao.findById(id);
        return stationDao.deleteById(station.getId());
    }
}
