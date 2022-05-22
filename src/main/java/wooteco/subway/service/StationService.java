package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.dto.station.StationResponse;

@Service
public class StationService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public StationService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public StationResponse createStation(String name) {
        return new StationResponse(stationDao.save(name));
    }

    public void deleteStation(Long stationId) {
        checkValidation(stationId);
        stationDao.deleteById(stationId);
    }

    private void checkValidation(Long stationId) {
        if (!sectionDao.findByStationId(stationId).isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 구간에 추가돼 있는 역은 삭제할 수 없습니다.");
        }
    }

    public List<StationResponse> findAll() {
        return stationDao.findAll().stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }
}
