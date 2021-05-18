package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.web.dto.StationRequest;
import wooteco.subway.web.dto.StationResponse;

@Service
public class StationService {

    private StationDao stationDao;
    private PathService pathService;

    public StationService(StationDao stationDao, PathService pathService) {
        this.stationDao = stationDao;
        this.pathService = pathService;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    public Station findStationById(Long id) {
        return stationDao.findById(id);
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
        pathService.syncPath();
    }
}
