package wooteco.subway.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.StationCreateRequest;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse createStation(final StationCreateRequest stationCreateRequest) {
        Station station = stationRepository.save(stationCreateRequest.toStation());

        return StationResponse.of(station);
    }

    public List<StationResponse> showStations() {
        return StationResponse.listOf(stationRepository.findAll());
    }

    public void removeStation(final Long id) {
        stationRepository.deleteById(id);
    }
}
