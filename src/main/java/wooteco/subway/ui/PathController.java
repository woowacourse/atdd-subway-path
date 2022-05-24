package wooteco.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.domain.DijkstraPath;
import wooteco.subway.service.PathService;
import wooteco.subway.service.dto.PathServiceResponse;
import wooteco.subway.ui.dto.PathRequest;
import wooteco.subway.ui.dto.PathResponse;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findShortestPath(@Validated @ModelAttribute PathRequest pathRequest) {
        PathServiceResponse pathServiceResponse =
                pathService.findShortestPath(pathRequest.toServiceRequest(), DijkstraPath::new);
        return ResponseEntity.ok(new PathResponse(pathServiceResponse));
    }
}
