package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.PathService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
public class PathController {

    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> retrieve(PathRequest pathRequest) throws UnsupportedEncodingException {

        String decodedSource = URLDecoder.decode(pathRequest.getSource(), "UTF-8");
        String decodedTarget = URLDecoder.decode(pathRequest.getTarget(), "UTF-8");

        PathResponse pathResponse = pathService.retrieveShortestPath(decodedSource, decodedTarget, pathRequest.getType());

        return ResponseEntity.ok(pathResponse);
    }
}
