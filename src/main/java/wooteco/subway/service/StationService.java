package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.request.StationRequest;
import wooteco.subway.dto.response.StationResponse;
import wooteco.subway.exception.DuplicateNameException;
import wooteco.subway.exception.NotFoundStationException;
import wooteco.subway.repository.StationRepository;

@Service
@Transactional
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse createStation(StationRequest station) {
        Station newStation = Station.from(station);
        validateDuplicateName(newStation);

        Long newStationId = stationRepository.save(newStation);
        return StationResponse.from(newStationId, newStation);
    }

    public List<StationResponse> getAllStations() {
        return stationRepository.findAll()
                .stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        validateExist(id);
        stationRepository.deleteById(id);
    }

    private void validateDuplicateName(Station station) {
        boolean isExisting = stationRepository.findByName(station.getName()).isPresent();

        if (isExisting) {
            throw new DuplicateNameException();
        }
    }

    private void validateExist(Long id) {
        boolean isExisting = stationRepository.findById(id).isPresent();

        if (!isExisting) {
            throw new NotFoundStationException();
        }
    }
}
