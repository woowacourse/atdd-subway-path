package wooteco.subway.path.ui;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathFindRequest;
import wooteco.subway.path.dto.PathResponse;

@RestController
@RequestMapping("/paths")
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@ModelAttribute @Valid PathFindRequest pathFindRequest) {
        PathResponse pathResponse = pathService.findStationInPath(pathFindRequest);

        return ResponseEntity.ok(pathResponse);
    }

}
