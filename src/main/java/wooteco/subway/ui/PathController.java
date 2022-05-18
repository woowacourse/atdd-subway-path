package wooteco.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.service.PathService;
import wooteco.subway.service.dto.PathServiceRequest;
import wooteco.subway.service.dto.PathServiceResponse;
import wooteco.subway.ui.dto.PathResponse;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findShortestPath(@RequestParam Long source,
                                                         @RequestParam Long target,
                                                         @RequestParam Integer age) {
        PathServiceRequest pathServiceRequest = new PathServiceRequest(source, target, age);
        PathServiceResponse pathServiceResponse = pathService.findShortestPath(pathServiceRequest);
        return ResponseEntity.ok(new PathResponse(pathServiceResponse));
    }
}
