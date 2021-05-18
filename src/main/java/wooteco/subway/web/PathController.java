package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.service.PathService;
import wooteco.subway.web.dto.response.PathResponse;

@RestController
@RequestMapping(value = "/api/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@RequestParam("source") Long sourceId, @RequestParam("target") Long targetId) {
        PathResponse response = pathService.findPath(sourceId, targetId);
        return ResponseEntity.ok(response);
    }
}
