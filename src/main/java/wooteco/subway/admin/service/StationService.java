package wooteco.subway.admin.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.CustomException;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;

@Service
public class StationService {
    private StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Station save(Station station) {
        try {
            return stationRepository.save(station);
        } catch (DbActionExecutionException exception) {
            if (exception.getCause() instanceof DuplicateKeyException) {
                throw new CustomException("이미 존재하는 호선입니다.", exception);
            }
            if (exception.getCause() instanceof DataIntegrityViolationException) {
                throw new CustomException("필수값을 입력해주세요.", exception);
            }
            throw exception;
        }
    }

    public List<Station> showStation() {
        return stationRepository.findAll();
    }

    public List<Station> findAll() {
        return stationRepository.findAll();
    }

    public void deleteById(Long id) {
        stationRepository.deleteById(id);
    }
}
