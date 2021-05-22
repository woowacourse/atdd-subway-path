package wooteco.subway.application;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.station.Station;
import wooteco.subway.exception.BadRequestException;
import wooteco.subway.presentation.dto.request.StationRequest;
import wooteco.subway.presentation.dto.response.StationResponse;
import wooteco.subway.repository.StationDao;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {
    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    public Station findStationById(Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new BadRequestException("해당하는 역이 존재하지 않습니다."));
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
    }
}
