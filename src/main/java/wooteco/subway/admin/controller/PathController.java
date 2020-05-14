package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.LineService;

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
    public ResponseEntity<PathResponse> findPath(@RequestParam("source") String encodedSourceName,
                                                 @RequestParam("target") String encodedTargetName,
                                                 @RequestParam("type") String type) throws UnsupportedEncodingException {
        String sourceName = URLDecoder.decode(encodedSourceName, "UTF-8");
        String targetName = URLDecoder.decode(encodedTargetName, "UTF-8");
        if ("DISTANCE".equals(type)) {
            PathResponse response = lineService.findShortestDistancePath(sourceName, targetName);
            return ResponseEntity.ok()
                    .body(response);
        }
        if ("DURATION".equals(type)) {
            PathResponse response = lineService.findShortestDistancePath(sourceName, targetName);
            return ResponseEntity.ok()
                    .body(response);
        }
        throw new IllegalArgumentException("잘못된 URI입니다.");
    }
}
