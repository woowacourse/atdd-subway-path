package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Station;
import wooteco.subway.repository.StationRepository;
import wooteco.subway.service.dto.StationResponse;
import wooteco.subway.service.dto.StationServiceRequest;

@Service
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse save(StationServiceRequest stationServiceRequest) {
        validateDuplicationName(stationServiceRequest.getName());
        Long savedId = stationRepository.save(new Station(stationServiceRequest.getName()));
        return new StationResponse(savedId, stationServiceRequest.getName());
    }

    private void validateDuplicationName(String name) {
        if (stationRepository.existsByName(name)) {
            throw new IllegalArgumentException("중복된 이름이 존재합니다.");
        }
    }

    public List<StationResponse> findAll() {
        List<Station> stationEntities = stationRepository.findAll();
        return stationEntities.stream()
            .map(i -> new StationResponse(i.getId(), i.getName()))
            .collect(Collectors.toList());
    }

    public boolean deleteById(Long id) {
        return stationRepository.deleteById(id);
    }
}
