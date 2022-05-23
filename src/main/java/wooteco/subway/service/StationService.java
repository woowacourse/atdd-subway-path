package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.repository.StationRepository;

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
            throw new DuplicateKeyException("이미 존재하는 역 이름입니다.");
        }
    }

    public StationResponse findById(Long id) {
        try {
            return StationResponse.from(stationRepository.findById(id));
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("존재하지 않는 역입니다.", 1);
        }
    }

    public List<StationResponse> findAll() {
        return stationRepository.findAll()
                .stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(Long id) {
        stationRepository.deleteById(id);
    }
}
