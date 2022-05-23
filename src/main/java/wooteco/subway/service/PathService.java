package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.Lines;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathFindingStrategy;
import wooteco.subway.dto.service.StationDto;
import wooteco.subway.dto.service.request.PathServiceRequest;
import wooteco.subway.dto.service.response.PathServiceResponse;

@Service
public class PathService {

    private static final String ERROR_MESSAGE_NOT_EXISTS_ID = "존재하지 않는 지하철 역입니다.";
    private final StationDao stationDao;
    private final LineRepository lineRepository;
    private final PathFindingStrategy pathFindingStrategy;
    private final Fare fare;

    public PathService(StationDao stationDao, LineRepository lineRepository,
        PathFindingStrategy pathFindingStrategy, Fare fare) {
        this.stationDao = stationDao;
        this.lineRepository = lineRepository;
        this.pathFindingStrategy = pathFindingStrategy;
        this.fare = fare;
    }

    public PathServiceResponse getShortestPath(PathServiceRequest pathServiceRequest) {
        validateNotExists(pathServiceRequest.getSource());
        validateNotExists(pathServiceRequest.getTarget());

        Station source = stationDao.getStation(pathServiceRequest.getSource());
        Station target = stationDao.getStation(pathServiceRequest.getTarget());

        Path path = new Path(new Lines(lineRepository.findAll()), pathFindingStrategy, source, target);
        List<StationDto> stationDtos = path.getShortestPath().stream()
            .map(station -> new StationDto(station.getId(), station.getName()))
            .collect(Collectors.toList());
        int distance = path.getShortestDistance();
        return new PathServiceResponse(stationDtos, distance, fare.calculate(path, pathServiceRequest.getAge()));
    }

    private void validateNotExists(Long id) {
        if (!stationDao.existById(id)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_EXISTS_ID);
        }
    }
}
