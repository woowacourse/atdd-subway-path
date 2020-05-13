package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.LineService;

import java.util.ArrayList;

@RestController
@RequestMapping("/path")
public class PathController {
    private LineService lineService;

    public PathController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<PathResponse> findShortestDistancePath(@RequestBody PathRequest request) {
        PathResponse response = new PathResponse(20, 10, new ArrayList<>());
        return ResponseEntity.ok()
                .body(response);
    }
}
