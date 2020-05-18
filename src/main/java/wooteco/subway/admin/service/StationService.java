package wooteco.subway.admin.service;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.StationCreateRequest;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.exception.DuplicatedNameException;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAll() {
        return StationResponse.listOf(stationRepository.findAll());
    }

    @Transactional
    public StationResponse save(final StationCreateRequest request) {
        String name = request.getName();
        validateDuplication(name);
        try {
            Station station = stationRepository.save(new Station(name));
            return StationResponse.of(station);
        } catch (DuplicateKeyException e) {
            throw new DuplicatedNameException(name);
        }
    }

    private void validateDuplication(final String name) {
        if (stationRepository.findByName(name).isPresent()) {
            throw new DuplicatedNameException(name);
        }
    }

    @Transactional
    public void deleteById(final Long id) {
        stationRepository.deleteById(id);
    }
}
