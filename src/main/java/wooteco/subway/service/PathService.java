package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.dao.repository.LineRepository;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.Fare;
import wooteco.subway.domain.path.Path;
import wooteco.subway.service.dto.PathResponse;
import wooteco.subway.service.dto.StationResponse;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public PathService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long sourceStationId, Long targetStationId, int age) {
        Path path = new Path(lineRepository.findAll(), getStation(stationService.findOne(sourceStationId)),
            getStation(stationService.findOne(targetStationId)));
        int distance = path.getDistance();
        return new PathResponse(path.getStations(), distance, Fare.of(distance, path.getExtraFare(), age).calculate());
    }

    private Station getStation(StationResponse stationResponse) {
        return new Station(stationResponse.getId(), stationResponse.getName());
    }
}
