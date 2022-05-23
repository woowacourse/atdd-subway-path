package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public StationService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        checkExistStationByName(stationRequest);
        final Station station = new Station(stationRequest.getName());
        final Long newStationId = stationDao.save(station);
        return new StationResponse(new Station(newStationId, station.getName()));
    }

    private void checkExistStationByName(StationRequest stationRequest) {
        if (stationDao.hasStation(stationRequest.getName())) {
            throw new IllegalArgumentException("같은 이름의 역이 존재합니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStations() {
        final List<Station> stations = stationDao.findAll();
        return stations.stream()
                .map(StationResponse::new)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public void deleteStation(Long id) {
        checkExistStationById(id);
        stationDao.deleteById(id);
    }

    private void checkExistStationById(Long id) {
        sectionDao.findByStationId(id).ifPresent(it -> {
            throw new IllegalArgumentException("해당 지하철역은 사용되고 있으므로 삭제할 수 없습니다.");
        });
        stationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "번에 해당하는 역이 존재하지 않습니다."));
    }
}
