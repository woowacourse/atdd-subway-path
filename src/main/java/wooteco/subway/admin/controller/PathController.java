package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.dto.PathSearchRequest;
import wooteco.subway.admin.dto.PathSearchResponse;
import wooteco.subway.admin.service.PathService;


@RequestMapping("/paths")
@RestController
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathSearchResponse> searchPath(PathSearchRequest pathSearchRequest) {
        return ResponseEntity.ok().body(pathService.searchPath(pathSearchRequest.getSource(), pathSearchRequest.getTarget(), pathSearchRequest.getType()));
    }
}
