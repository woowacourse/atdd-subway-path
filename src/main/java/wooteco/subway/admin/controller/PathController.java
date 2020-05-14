package wooteco.subway.admin.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathRequestWithId;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.PathService;
import wooteco.subway.admin.service.StationService;

@RestController
@RequestMapping("/path")
public class PathController {

    private final PathService pathService;
    private final StationService stationService;

    public PathController(PathService pathService,
        StationService stationService) {
        this.pathService = pathService;
        this.stationService = stationService;
    }

    @PostMapping
    ResponseEntity<PathResponse> findPath(@Valid @RequestBody PathRequest pathRequest) {
        PathRequestWithId PathRequestWithId = stationService.toPathRequestWithId(pathRequest);
        PathResponse response = pathService.findPath(PathRequestWithId);

        return ResponseEntity.ok(response);
    }
}
