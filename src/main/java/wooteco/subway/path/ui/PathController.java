package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.path.dto.PathRequest;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.service.PathService;

import javax.validation.Valid;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findShortestPath(@ModelAttribute @Valid PathRequest pathRequest) {
        PathResponse pathResponse = pathService.findShortestPath(pathRequest.getSource(), pathRequest.getTarget());
        return ResponseEntity.ok(pathResponse);
    }
}
