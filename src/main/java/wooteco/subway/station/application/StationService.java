package wooteco.subway.station.application;

import org.springframework.stereotype.Service;
import wooteco.subway.station.application.dto.StationResponseDto;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.infrastructure.dao.StationDao;
import wooteco.subway.station.ui.dto.StationRequest;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponseDto saveStation(StationRequest stationRequest) {
        Station station = stationDao.insert(stationRequest.toStation());

        return StationResponseDto.of(station);
    }

    public Station findStationById(Long id) {
        return stationDao.findById(id);
    }

    public List<StationResponseDto> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponseDto::of)
                .collect(toList());
    }

    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
    }

}
