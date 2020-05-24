package wooteco.subway.admin.path.controller;

import javax.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.path.service.PathService;
import wooteco.subway.admin.path.service.dto.PathInfoResponse;

@RequestMapping("/paths")
@Validated
@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathInfoResponse> searchPath(
        @RequestParam(value = "source") @NotNull(message = "출발역이 존재하지 않습니다.") Long source,
        @RequestParam("target") @NotNull(message = "도착역이 존재하지 않습니다.") Long target) {
        PathInfoResponse pathInfoResponse = pathService.searchPath(source, target);

        return ResponseEntity
            .ok()
            .body(pathInfoResponse);
    }

}
