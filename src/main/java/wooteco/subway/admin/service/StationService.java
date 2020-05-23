package wooteco.subway.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.StationCreateRequest;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.exception.StationNotFoundException;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse createStation(StationCreateRequest stationCreateRequest) {
        Station station = stationCreateRequest.toStation();
        Station createdStation = stationRepository.save(station);
        return StationResponse.of(createdStation);
    }

    public List<StationResponse> findAll() {
        List<Station> stations = stationRepository.findAll();
        return StationResponse.listOf(stations);
    }

    public StationResponse findStationById(Long stationId) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(StationNotFoundException::new);
        return StationResponse.of(station);
    }

    @Transactional
    public void deleteById(Long stationId) {
        stationRepository.deleteById(stationId);
    }
}
