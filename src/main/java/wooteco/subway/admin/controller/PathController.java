package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.PathService;

@RestController
@RequestMapping("/paths")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@ModelAttribute PathRequest pathRequest) {
        PathResponse pathResponse = pathService.generatePathResponse(pathRequest.getSource(), pathRequest.getTarget());
        return ResponseEntity.ok().body(pathResponse);
    }
}
