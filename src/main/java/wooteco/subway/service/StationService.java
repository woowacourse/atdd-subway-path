package wooteco.subway.service;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.exception.station.DuplicateStationNameException;
import wooteco.subway.exception.station.NoSuchStationException;
import wooteco.subway.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse save(StationRequest stationRequest) {
        try {
            Station station = stationRequest.toStation();
            return StationResponse.from(stationRepository.save(station));
        } catch (DuplicateKeyException e) {
            throw new DuplicateStationNameException();
        }
    }

    public StationResponse findById(Long stationId) {
        try {
            return StationResponse.from(stationRepository.findById(stationId));
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchStationException();
        }
    }

    public List<StationResponse> findAll() {
        return stationRepository.findAll()
                .stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(Long stationId) {
        stationRepository.deleteById(stationId);
    }
}
