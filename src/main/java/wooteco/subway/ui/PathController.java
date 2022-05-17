package wooteco.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.service.SectionService;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final SectionService sectionService;

    public PathController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPaths(@ModelAttribute PathRequest pathRequest) {
        PathResponse pathResponse = sectionService.calculateMinDistance(pathRequest);
        return ResponseEntity.ok(pathResponse);
    }
}
