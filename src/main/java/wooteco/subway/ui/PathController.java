package wooteco.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.PathSearchModel;
import wooteco.subway.service.PathService;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> getPath(@ModelAttribute PathSearchModel pathSearchModel) {
        PathResponse pathResponse = pathService.findPath(pathSearchModel.getSource(), pathSearchModel.getTarget(),
                pathSearchModel.getAge());
        return ResponseEntity.ok(pathResponse);
    }
}
