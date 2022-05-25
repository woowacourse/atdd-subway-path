package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.fare.FareCalculator;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.Lines;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathFindingStrategy;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.dto.service.StationDto;
import wooteco.subway.dto.service.request.PathServiceRequest;
import wooteco.subway.dto.service.response.PathServiceResponse;

@Service
public class PathService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final PathFindingStrategy pathFindingStrategy;

    public PathService(StationRepository stationRepository, LineRepository lineRepository,
        PathFindingStrategy pathFindingStrategy) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.pathFindingStrategy = pathFindingStrategy;
    }

    @Transactional
    public PathServiceResponse getShortestPath(PathServiceRequest pathServiceRequest) {
        Station source = stationRepository.find(pathServiceRequest.getSource());
        Station target = stationRepository.find(pathServiceRequest.getTarget());

        Path path = new Path(new Lines(lineRepository.findAll()), pathFindingStrategy, source, target);
        List<StationDto> stationDtos = path.getShortestPath().stream()
            .map(station -> new StationDto(station.getId(), station.getName()))
            .collect(Collectors.toList());
        int distance = path.getShortestDistance();
        return new PathServiceResponse(stationDtos, distance,
            FareCalculator.calculate(path, pathServiceRequest.getAge()));
    }
}
