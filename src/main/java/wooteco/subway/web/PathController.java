package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.service.PathService;
import wooteco.subway.web.dto.PathResponse;

@RestController
@RequestMapping("/api/paths")
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> getPath(
        @RequestParam("source") Long sourceStationId,
        @RequestParam("target") Long targetStationId
    ) {
        PathResponse response = pathService.getShortestPath(sourceStationId, targetStationId);
        return ResponseEntity.ok().body(response);
    }
}
