package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.controller.dto.PathRequest;
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

    public PathResponse findPath(PathRequest request) {
        Station source = getStation(request.getSource());
        Station target = getStation(request.getTarget());
        Path path = new Path(lineRepository.findAll(), source, target);
        int distance = path.getDistance();
        int fare = Fare.of(distance, path.getExtraFare(), request.getAge()).calculate();
        return PathResponse.of(path, fare);
    }

    private Station getStation(Long stationId) {
        StationResponse stationResponse = stationService.findOne(stationId);
        return new Station(stationResponse.getId(), stationResponse.getName());
    }
}
