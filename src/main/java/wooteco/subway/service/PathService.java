package wooteco.subway.service;

import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.Fare;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.graph.SubwayGraph;
import wooteco.subway.infrastructure.jgraph.SubwayJGraph;
import wooteco.subway.service.dto.request.PathRequest;
import wooteco.subway.service.dto.response.PathResponse;
import wooteco.subway.service.dto.response.ResponseAssembler;

@Service
public class PathService {

    private final LineService lineService;
    private final SubwayGraph subwayGraph;
    private final IntegrityValidator integrityValidator;
    private final ResponseAssembler responseAssembler;

    public PathService(LineService lineService, SubwayJGraph subwayGraph,
                       IntegrityValidator integrityValidator, ResponseAssembler responseAssembler) {
        this.lineService = lineService;
        this.subwayGraph = subwayGraph;
        this.integrityValidator = integrityValidator;
        this.responseAssembler = responseAssembler;
    }

    public PathResponse findPath(PathRequest pathRequest) {
        validateStationsExist(pathRequest);

        Path shortestPath = subwayGraph.calculateShortestPath(
                responseAssembler.lines(lineService.findAll()),
                pathRequest.getSourceStationId(), pathRequest.getTargetStationId());

        Fare fare = shortestPath.calculateFare();
        return responseAssembler.pathResponse(shortestPath, fare);
    }

    private void validateStationsExist(PathRequest pathRequest) {
        Stream.of(pathRequest.getSourceStationId(), pathRequest.getTargetStationId())
                .forEach(integrityValidator::validateStationExist);
    }
}
