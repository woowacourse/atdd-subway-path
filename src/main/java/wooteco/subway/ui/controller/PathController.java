package wooteco.subway.ui.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.dto.request.PathRequest;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.ui.service.PathService;

@RestController
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> getPath(
            @ModelAttribute PathRequest pathRequest, @RequestParam int age) {
        PathResponse pathResponse = pathService.getPath(pathRequest);
        return ResponseEntity.ok().body(pathResponse);
    }
}
