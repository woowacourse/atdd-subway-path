package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.CreateStationRequest;
import wooteco.subway.dto.response.StationResponse;
import wooteco.subway.exception.duplicate.DuplicateStationException;
import wooteco.subway.exception.notfound.NotFoundStationException;
import wooteco.subway.repository.StationRepository;

@Service
@Transactional
public class StationService {

    private final StationRepository repository;

    public StationService(final StationRepository repository) {
        this.repository = repository;
    }

    public StationResponse createStation(final CreateStationRequest request) {
        try {
            final Station station = new Station(request.getName());
            final Long id = repository.save(station);
            final Station savedStation = repository.getById(id);
            return StationResponse.from(savedStation);
        } catch (final DuplicateKeyException e) {
            throw new DuplicateStationException();
        }
    }

    @Transactional(readOnly = true)
    public List<StationResponse> showStations() {
        final List<Station> stations = repository.findAll();
        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Station show(final Long id) {
        return repository.getById(id);
    }

    public void deleteStation(final Long id) {
        validateNotExistStation(id);
        repository.deleteById(id);
    }

    public void validateNotExistStation(final Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundStationException();
        }
    }
}
