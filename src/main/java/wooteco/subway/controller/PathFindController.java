package wooteco.subway.controller;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.dto.path.PathFindRequest;
import wooteco.subway.dto.path.PathFindResponse;
import wooteco.subway.service.PathService;

@RestController
public class PathFindController {

    private final PathService pathService;

    public PathFindController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathFindResponse> findPath(@ModelAttribute @Valid PathFindRequest pathFindRequest) {
        PathFindResponse response = pathService.findPath(pathFindRequest);
        return ResponseEntity.ok(response);
    }
}
