package wooteco.subway.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.controller.dto.request.PathRequestForm;
import wooteco.subway.controller.dto.request.RequestFormAssembler;
import wooteco.subway.controller.dto.response.PathResponseForm;
import wooteco.subway.controller.dto.response.ResponseFormAssembler;
import wooteco.subway.service.PathService;
import wooteco.subway.service.StationService;
import wooteco.subway.service.dto.request.PathRequest;
import wooteco.subway.service.dto.response.PathResponse;
import wooteco.subway.service.dto.response.StationResponse;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;
    private final StationService stationService;
    private final RequestFormAssembler requestFormAssembler;
    private final ResponseFormAssembler responseFormAssembler;

    public PathController(PathService pathService,StationService stationService,
                          RequestFormAssembler requestFormAssembler,
                          ResponseFormAssembler responseFormAssembler) {
        this.pathService = pathService;
        this.stationService = stationService;
        this.requestFormAssembler = requestFormAssembler;
        this.responseFormAssembler = responseFormAssembler;
    }

    @GetMapping
    public ResponseEntity<PathResponseForm> findPath(@ModelAttribute PathRequestForm pathRequestForm) {
        PathRequest pathRequest = requestFormAssembler.pathRequest(pathRequestForm);
        PathResponse pathResponse = pathService.findPath(pathRequest);
        PathResponseForm pathResponseForm = getPathResponseForm(pathResponse);
        return ResponseEntity.ok(pathResponseForm);
    }

    private PathResponseForm getPathResponseForm(PathResponse pathResponse) {
        List<StationResponse> stationResponses = pathResponse.getStationIds()
                .stream()
                .map(stationService::findById)
                .collect(Collectors.toUnmodifiableList());
        return responseFormAssembler.pathResponseForm(pathResponse, stationResponses);
    }
}
