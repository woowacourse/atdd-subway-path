package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.line.application.LineService;
import wooteco.subway.path.dto.PathResponse;

@RestController
@RequestMapping("/paths")
public class PathController {

    private LineService lineService;

    public PathController(LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findShortPath(@RequestParam("source") Long source, @RequestParam("target") Long target) {
        PathResponse shortPath = lineService.findShortPath(source, target);
        return ResponseEntity.ok(shortPath);
    }
}
