package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.station.Station;
import wooteco.subway.exception.DuplicateStationNameException;
import wooteco.subway.exception.NoSuchStationException;
import wooteco.subway.repository.StationRepository;
import wooteco.subway.ui.dto.response.StationResponse;

@Transactional
@Service
public class SpringStationService implements StationService {

    private final StationRepository stationRepository;

    public SpringStationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public StationResponse create(String name) {
        validateDuplicateName(name);
        Station saved = stationRepository.save(new Station(name));

        return toStationResponse(saved);
    }

    private void validateDuplicateName(String name) {
        if (stationRepository.existsByName(name)) {
            throw new DuplicateStationNameException(name);
        }
    }

    private StationResponse toStationResponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    @Transactional(readOnly = true)
    @Override
    public List<StationResponse> findAll() {
        return stationRepository.findAll()
                .stream()
                .map(this::toStationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        validateExistsById(id);

        stationRepository.deleteById(id);
    }

    private void validateExistsById(Long id) {
        if (stationRepository.existsById(id)) {
            return;
        }

        throw new NoSuchStationException(id);
    }
}
