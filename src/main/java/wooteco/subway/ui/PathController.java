package wooteco.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.domain.Path;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.service.PathService;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> createPath(@RequestParam(name = "source") final Long source,
                                                   @RequestParam(name = "target") final Long target,
                                                   @RequestParam(name = "age") final int age) {
        final Path path = pathService.createPath(source, target, age);
        final PathResponse pathResponse = PathResponse.from(path);

        return ResponseEntity.ok().body(pathResponse);
    }
}
