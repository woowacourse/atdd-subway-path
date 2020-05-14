package wooteco.subway.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathRequestWithId;
import wooteco.subway.admin.dto.PathType;
import wooteco.subway.admin.dto.StationCreateRequest;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.exception.IllegalStationNameException;
import wooteco.subway.admin.exception.NotFoundPathException;
import wooteco.subway.admin.exception.NotFoundStationException;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse save(StationCreateRequest request) {
        Station persistStation = stationRepository.save(request.toStation());

        return StationResponse.of(persistStation);
    }

    public List<StationResponse> findAll() {
        List<Station> stations = stationRepository.findAll();

        return StationResponse.listOf(stations);
    }

    public void deleteById(Long id) {
        stationRepository.deleteById(id);
    }


    public Long findIdByName(String name) {
        return stationRepository.findByName(name)
            .orElseThrow(() -> new NotFoundStationException(name)).getId();
    }

    public List<Station> findAllById(List<Long> lineStationsId) {
        return stationRepository.findAllById(lineStationsId);
    }
}
