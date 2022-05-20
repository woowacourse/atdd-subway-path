package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.dao.repository.LineRepository;
import wooteco.subway.domain.Lines;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Station;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public PathService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public Path findPath(Long sourceStationId, Long targetStationId) {
        Station source = stationService.findOne(sourceStationId);
        Station target = stationService.findOne(targetStationId);
        Lines lines = new Lines(lineRepository.findAll());
        return lines.findPath(source, target);
    }
}
