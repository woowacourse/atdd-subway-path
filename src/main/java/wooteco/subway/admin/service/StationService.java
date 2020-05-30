package wooteco.subway.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.request.StationCreateRequest;
import wooteco.subway.admin.dto.response.StationResponse;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<StationResponse> showStations() {
        return StationResponse.listOf(stationRepository.findAll());
    }

    @Transactional(readOnly = true)
    public StationResponse createStation(StationCreateRequest request) {
        Station station = request.toStation();
        return StationResponse.of(stationRepository.save(station));
    }

    public void deleteStation(Long id) {
        stationRepository.deleteById(id);
    }
}
