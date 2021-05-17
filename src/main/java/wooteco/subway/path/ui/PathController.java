package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.path.dto.PathResponse;

@RestController
@RequestMapping("/paths")
public class PathController {

    @PostMapping
    public ResponseEntity<PathResponse> findPath(@RequestParam("source") long sourceStationId,
        @RequestParam("target") long targetStationId) {
        PathResponse pathResponse = new PathResponse();
        return ResponseEntity.ok()
            .body(pathResponse);
    }
}
