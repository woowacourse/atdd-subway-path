package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathIdModel;
import wooteco.subway.path.dto.PathResponse;

@RestController
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@ModelAttribute PathIdModel pathIdModel) {
        PathResponse pathResponse = pathService.findShortestPath(pathIdModel.getSource(), pathIdModel.getTarget());
        return ResponseEntity.ok(pathResponse);
    }
}
