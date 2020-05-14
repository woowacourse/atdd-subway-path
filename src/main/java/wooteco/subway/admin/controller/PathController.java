package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.PathService;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity findPath(@RequestParam("source") String source, @RequestParam("target") String target, @RequestParam("type") String type) {
        if(source.equals(target)){
            return ResponseEntity.badRequest().body(new IllegalArgumentException("출발역과 도착역은 동일할 수 없습니다."));
        }
        pathService.findPath(source, target);
        return ResponseEntity.ok().body(pathService.findPath(source, target));
    }
}
