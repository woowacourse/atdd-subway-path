package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.StationCreateRequest;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse createStation(final StationCreateRequest stationRequest) {
        Station station = stationRepository.save(stationRequest.toStation());
        return StationResponse.from(station);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> showStations() {
        return StationResponse.listFrom(stationRepository.findAll());
    }

    @Transactional
    public void deleteStation(final Long id) {
        stationRepository.deleteById(id);
    }
}
