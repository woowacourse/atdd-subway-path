package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.PathService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> retrieve(@RequestParam(name = "source", defaultValue = "") String source,
                                                 @RequestParam(name = "target", defaultValue = "") String target,
                                                 @RequestParam(name = "type") PathType pathType) throws UnsupportedEncodingException {

        String decodedSource = URLDecoder.decode(source, "UTF-8");
        String decodedTarget = URLDecoder.decode(target, "UTF-8");

        PathResponse pathResponse = pathService.retrieveShortestPath(decodedSource, decodedTarget, pathType);

        return ResponseEntity.ok(pathResponse);
    }
}
