package wooteco.subway.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.application.LineService;
import wooteco.subway.dto.PathResponse;

@RequestMapping("/paths")
@RestController
public class PathController {

    private final LineService lineService;

    public PathController(final LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@RequestParam Long source, @RequestParam Long target, @RequestParam int age) {
        PathResponse response = lineService.findPath(source, target, age);
        return ResponseEntity.ok(response);
    }
}
