package wooteco.subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.application.PathService;
import wooteco.subway.dto.response.PathResponse;

@RequestMapping("/paths")
@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PathResponse findPath(@RequestParam Long source, @RequestParam Long target) {
        return pathService.findPath(source, target);
    }
}
