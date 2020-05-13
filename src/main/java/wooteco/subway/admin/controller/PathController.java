package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.LineService;

@RestController
@RequestMapping("/path")
public class PathController {
    private LineService lineService;

    public PathController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<PathResponse> findShortestDistancePath(@RequestBody PathRequest request) {
        return ResponseEntity.ok()
                .body(lineService.findShortestDistancePath(request));
    }
}
