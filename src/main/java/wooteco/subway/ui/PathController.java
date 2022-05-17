package wooteco.subway.ui;

import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.service.LineService;
import wooteco.subway.service.PathService;
import wooteco.subway.service.dto.LineServiceResponse;
import wooteco.subway.service.dto.PathServiceResponse;
import wooteco.subway.ui.dto.PathRequest;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathServiceResponse> findShortestPath(@RequestParam Long source, @RequestParam Long target, @RequestParam int age) {
        PathRequest pathRequest = new PathRequest(source, target, age);
        PathServiceResponse response = pathService.findShortestPath(
            pathRequest.toPathServiceRequest());
        return ResponseEntity.ok().body(response);
    }
}
