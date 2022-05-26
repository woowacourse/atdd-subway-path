package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.dao.StationDao;
import wooteco.subway.dao.repository.SectionRepository;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.StationResponse;

@Service
@Transactional(readOnly = true)
public class StationService {

    private final StationDao stationDao;
    private final SectionRepository sectionRepository;

    public StationService(StationDao stationDao, SectionRepository sectionRepository) {
        this.stationDao = stationDao;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public StationResponse create(String name) {
        validateNameNotDuplicated(name);
        Long stationId = stationDao.save(new Station(name));
        return StationResponse.from(stationDao.findById(stationId));
    }

    private void validateNameNotDuplicated(String name) {
        if (stationDao.existsByName(name)) {
            throw new IllegalArgumentException("해당 이름의 지하철 역이 이미 존재합니다.");
        }
    }

    public List<StationResponse> findAllStations() {
        return stationDao.findAll().stream()
            .map(StationResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public void remove(Long id) {
        if (sectionRepository.existByStation(id)) {
            throw new IllegalArgumentException("구간으로 등록되어 있어 삭제할 수 없습니다.");
        }
        stationDao.remove(id);
    }

    public StationResponse findOne(Long id) {
        return StationResponse.from(stationDao.findById(id));
    }
}
