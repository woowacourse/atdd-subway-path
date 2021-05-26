package wooteco.subway.station.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.routemap.application.RouteMapManager;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

@Service
public class StationService {

    private final StationDao stationDao;
    private final RouteMapManager routeMapManager;

    public StationService(StationDao stationDao, RouteMapManager routeMapManager) {
        this.stationDao = stationDao;
        this.routeMapManager = routeMapManager;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationDao.insert(stationRequest.toStation());
        routeMapManager.addStation(station);
        return StationResponse.of(station);
    }

    public Station findStationById(Long id) {
        return stationDao.findById(id);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();
        return StationResponse.listOf(stations);
    }

    @Transactional
    public void deleteStationById(Long id) {
        Station station = findStationById(id);
        stationDao.deleteById(id);
        routeMapManager.removeStation(station);
    }
}
