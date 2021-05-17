package wooteco.subway.path.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.path.dto.PathRequest;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.service.PathService;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findShortestPath(@ModelAttribute PathRequest pathRequest) {
        long sourceStationId = pathRequest.getSource();
        long targetStationId = pathRequest.getTarget();
        PathResponse pathResponse = pathService.findShortestPath(sourceStationId, targetStationId);
        return ResponseEntity.ok(pathResponse);
    }
}
