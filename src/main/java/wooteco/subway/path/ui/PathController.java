package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.dto.PathRequest;
import wooteco.subway.path.service.PathService;

@RestController
@RequestMapping("/paths")
public class PathController {

    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@RequestParam("source") long sourceStationId,
        @RequestParam("target") long targetStationId) {
        PathRequest pathRequest = new PathRequest(sourceStationId, targetStationId);
        PathResponse pathResponse = pathService.optimalPath(pathRequest);
        return ResponseEntity.ok()
            .body(pathResponse);
    }
}
