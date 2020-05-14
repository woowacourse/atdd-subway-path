package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.domain.CriteriaType;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.PathService;

@RestController
@RequestMapping("/path")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @PostMapping
    public ResponseEntity<PathResponse> findPath(@RequestBody PathRequest pathRequest) {
        PathResponse pathResponse = pathService.showPaths(pathRequest.getSource(),
            pathRequest.getTarget(), CriteriaType.of(pathRequest.getCriteria()));
        return ResponseEntity.ok(pathResponse);
    }
}
