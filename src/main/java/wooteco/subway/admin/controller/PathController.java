package wooteco.subway.admin.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.ShortestPathResponse;
import wooteco.subway.admin.exception.InvalidRequestDataException;
import wooteco.subway.admin.service.PathService;

@RestController
@RequestMapping("/paths")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    ResponseEntity<ShortestPathResponse> getShortestPath(@Valid PathRequest request, Errors errors) {
        if (errors.hasErrors()) {
            throw new InvalidRequestDataException("경로 탐색시 필수 값이 빠져있습니다.");
        }
        return ResponseEntity.ok(pathService.findShortestPath(request));
    }
}
