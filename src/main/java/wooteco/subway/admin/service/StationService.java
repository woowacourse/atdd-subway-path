package wooteco.subway.admin.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public StationResponse createStation(final StationCreateRequest stationCreateRequest) {
        Station station = stationRepository.save(stationCreateRequest.toStation());

        return StationResponse.of(station);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> showStations() {
        return StationResponse.listOf(stationRepository.findAll());
    }

    @Transactional(readOnly = true)
    public StationResponse showStation(Long id){
        Station station = stationRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("역을 찾을 수 없습니다."));

        return StationResponse.of(station);
    }

    @Transactional
    public void removeStation(final Long id) {
        stationRepository.deleteById(id);
    }
}