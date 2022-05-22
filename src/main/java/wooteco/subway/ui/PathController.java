package wooteco.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.service.PathService;
import wooteco.subway.service.dto.request.PathServiceRequest;
import wooteco.subway.service.dto.response.PathServiceResponse;
import wooteco.subway.ui.dto.response.PathResponse;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@RequestParam Integer source, @RequestParam Integer target,
                                                 @RequestParam Integer age) {
        final PathServiceRequest pathServiceRequest = new PathServiceRequest(source, target, age);
        final PathServiceResponse path = pathService.findPath(pathServiceRequest);

        return ResponseEntity.ok(PathResponse.from(path));
    }
}
