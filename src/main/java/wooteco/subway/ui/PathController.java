package wooteco.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.service.SectionService;

@RestController
public class PathController {

    private final SectionService sectionService;

    public PathController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> showPath(@RequestParam Long source, @RequestParam Long target, @RequestParam Long age) {
        PathResponse pathResponse = sectionService.findShortestPath(source, target);
        return ResponseEntity.ok().body(pathResponse);
    }
}
