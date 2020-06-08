package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.repository.StationRepository;
import wooteco.subway.admin.service.errors.PathException;

import java.util.List;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<Station> findAll() {
        return stationRepository.findAll();
    }

    @Transactional
    public Station save(Station station) {
        return stationRepository.save(station);
    }

    @Transactional
    public void deleteById(Long id) {
        stationRepository.deleteById(id);
    }

    public List<Station> findAllById(List<Long> path) {
        return stationRepository.findAllById(path);
    }

    public Station findByName(String source) {
        return stationRepository.findByName(source).orElseThrow(() -> new PathException("해당 역을 찾을 수 없습니다."));
    }
}
