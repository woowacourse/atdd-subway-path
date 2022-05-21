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
        var station = stationDao.save(name);

        return new StationResponse(station.getId(), station.getName());
    }

    public void deleteStation(Long id) {
        checkValidation(id);
        stationDao.deleteById(id);
    }

    private void checkValidation(Long id) {
        if (!sectionDao.findByStationId(id).isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 구간에 추가돼 있는 역은 삭제할 수 없습니다.");
        }
    }

    public List<StationResponse> findAll() {
        return stationDao.findAll().stream()
                .map(it -> new StationResponse(it.getId(), it.getName()))
                .collect(Collectors.toList());
    }
}
