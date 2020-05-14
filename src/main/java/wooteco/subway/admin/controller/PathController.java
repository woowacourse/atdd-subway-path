package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.dto.SearchPathRequest;
import wooteco.subway.admin.dto.SearchPathResponse;
import wooteco.subway.admin.service.PathService;

@RestController
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @PostMapping("/paths")
    public ResponseEntity<SearchPathResponse> searchPath(@RequestBody SearchPathRequest searchPathRequest) {
        String startStationName = searchPathRequest.getStartStationName();
        String targetStationName = searchPathRequest.getTargetStationName();
        String type = searchPathRequest.getType();

        return ResponseEntity.ok().body(pathService.searchPath(startStationName, targetStationName, type));
    }
}
