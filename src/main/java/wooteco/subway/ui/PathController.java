package wooteco.subway.ui;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.dto.path.PathRequest;
import wooteco.subway.dto.path.PathResponse;
import wooteco.subway.service.PathService;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> showPath(@ModelAttribute @Valid PathRequest request) {
        final PathResponse response = pathService.find(request.getSource(), request.getTarget(), request.getAge());
        return ResponseEntity.ok().body(response);
    }
}
