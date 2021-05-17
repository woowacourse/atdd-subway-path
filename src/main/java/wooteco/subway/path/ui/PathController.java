package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.apllication.PathService;

@RestController
@RequestMapping("/path")
public class PathController {
    // TODO: 경로조회 기능 구현하기
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity getSubwayPath(@RequestParam Long source, @RequestParam Long target) {
        PathResponse response = pathService.findShortestPath(source, target);
        return ResponseEntity.ok().body(response);
    }

}
