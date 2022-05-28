package wooteco.subway.service;

import org.springframework.stereotype.Component;

import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.exception.NoSuchStationException;

@Component
public class IntegrityValidator {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public IntegrityValidator(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public void validateStationExist(long stationId) {
        if (!stationRepository.existsById(stationId)) {
            throw new NoSuchStationException(stationId);
        }
    }

    public void validateSectionNotExistByStationId(long stationId) {
        if (lineRepository.existsSectionByStationId(stationId)) {
            throw new IllegalStateException("[지하철역을 포함하는 구간이 존재합니다.");
        }
    }
}
