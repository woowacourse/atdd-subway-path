package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.domain.path.PathType;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.LineService;

import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/paths")
public class PathController {
    private LineService lineService;

    public PathController(LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@RequestParam("source") @NotNull String sourceName,
                                                 @RequestParam("target") @NotNull String targetName,
                                                 @RequestParam("type") @NotNull String type) throws UnsupportedEncodingException {
        PathResponse response = lineService.findShortestPath(sourceName, targetName, PathType.of(type));
        return ResponseEntity.ok()
                .body(response);
    }
}
