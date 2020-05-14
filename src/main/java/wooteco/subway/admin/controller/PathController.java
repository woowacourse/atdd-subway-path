package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.domain.path.Type;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.LineService;

import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
@RequestMapping("/paths")
public class PathController {
    private LineService lineService;

    public PathController(LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@RequestParam("source") @NotNull String encodedSourceName,
                                                 @RequestParam("target") @NotNull String encodedTargetName,
                                                 @RequestParam("type") @NotNull String type) throws UnsupportedEncodingException {
        String sourceName = URLDecoder.decode(encodedSourceName, "UTF-8");
        String targetName = URLDecoder.decode(encodedTargetName, "UTF-8");

        PathResponse response = lineService.findShortestPath(sourceName, targetName, Type.of(type));
        return ResponseEntity.ok()
                .body(response);
    }
}
