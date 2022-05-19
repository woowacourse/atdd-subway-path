package wooteco.subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.dto.controller.response.PathResponse;
import wooteco.subway.dto.converter.PathConverter;
import wooteco.subway.dto.service.PathServiceRequest;
import wooteco.subway.service.PathService;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> getShortestPath(@RequestParam Long source, @RequestParam Long target,
                                                        @RequestParam Integer age) {
        PathServiceRequest pathServiceRequest = PathConverter.toServiceRequest(source, target, age);
        PathResponse pathResponse = PathConverter.toResponse(pathService.getShortestPath(pathServiceRequest));
        return ResponseEntity.ok(pathResponse);
    }
}
