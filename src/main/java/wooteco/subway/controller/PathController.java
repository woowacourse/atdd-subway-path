package wooteco.subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.controller.dto.response.PathResponse;
import wooteco.subway.service.PathService;

@RequestMapping("/api/paths")
@RestController
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@RequestParam(name = "source") Long sourceStationId,
                                                 @RequestParam(name = "target") Long targetStationId) {

        PathResponse pathResponse = pathService.findPath(sourceStationId, targetStationId);
        return ResponseEntity.ok(pathResponse);
    }
}
