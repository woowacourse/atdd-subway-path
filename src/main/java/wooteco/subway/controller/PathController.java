package wooteco.subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.service.dto.request.PathRequest;
import wooteco.subway.service.dto.response.PathResponse;
import wooteco.subway.service.PathService;

import javax.validation.Valid;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> showPath(@ModelAttribute @Valid PathRequest pathRequest) {
        final PathResponse pathResponse = pathService.findShortestPath(pathRequest.getSource(), pathRequest.getTarget());
        return ResponseEntity.ok().body(pathResponse);
    }
}
