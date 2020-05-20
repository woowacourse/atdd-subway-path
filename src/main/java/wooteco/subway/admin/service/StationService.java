package wooteco.subway.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.StationCreateRequest;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.exception.NotFoundStationException;
import wooteco.subway.admin.repository.StationRepository;

@Service
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse save(StationCreateRequest request) {
        Station persistStation = stationRepository.save(request.toStation());

        return StationResponse.of(persistStation);
    }

    public List<StationResponse> findAll() {
        List<Station> stations = stationRepository.findAll();

        return StationResponse.listOf(stations);
    }


    public List<Station> findAllById(List<Long> lineStationsId) {
        return stationRepository.findAllById(lineStationsId);
    }

    public Long findIdByName(String name) {
        return stationRepository.findByName(name)
            .orElseThrow(() -> new NotFoundStationException(name)).getId();
    }

    @Transactional
    public void deleteById(Long id) {
        stationRepository.deleteById(id);
    }
}
