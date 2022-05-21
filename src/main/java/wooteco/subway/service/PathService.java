package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.dao.repository.LineRepository;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Path;
import wooteco.subway.service.dto.PathDto;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public PathService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public PathDto findPath(Long sourceStationId, Long targetStationId, int age) {
        Path path = new Path(lineRepository.findAll(), stationService.findOne(sourceStationId),
            stationService.findOne(targetStationId));
        int distance = path.getDistance();
        return new PathDto(path.getStations(), distance, Fare.of(distance, path.getOverFare(), age).calculate());
    }
}
