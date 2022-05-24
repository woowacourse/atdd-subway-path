package wooteco.subway.service;

import java.util.List;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.DomainException;
import wooteco.subway.exception.ExceptionMessage;
import wooteco.subway.repository.StationRepository;
import wooteco.subway.service.dto.StationRequest;

@Service
@Transactional
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Station create(StationRequest request) {
        Station station = new Station(request.getName());
        try {
            return stationRepository.save(station);
        } catch (DuplicateKeyException e) {
            throw new DomainException(ExceptionMessage.DUPLICATED_STATION_NAME.getContent());
        }
    }

    @Transactional(readOnly = true)
    public List<Station> findAll() {
        return stationRepository.findAll();
    }

    public void delete(Long id) {
        stationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Station findById(Long id) {
        return stationRepository.findById(id);
    }
}
