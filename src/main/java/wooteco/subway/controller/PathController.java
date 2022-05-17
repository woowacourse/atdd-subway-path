package wooteco.subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.controller.dto.ControllerDtoAssembler;
import wooteco.subway.controller.dto.path.PathResponse;
import wooteco.subway.service.PathService;
import wooteco.subway.service.dto.PathRequestDto;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@RequestParam Long source, @RequestParam Long target, @RequestParam int age) {
        PathResponse pathResponse = ControllerDtoAssembler.pathResponse(pathService.getPath(new PathRequestDto(source, target, age)));
        return ResponseEntity.ok().body(pathResponse);
    }
}
