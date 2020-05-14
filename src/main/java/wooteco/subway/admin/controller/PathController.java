package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.LineService;
import wooteco.subway.admin.service.PathService;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@Validated
public class PathController {
    private final LineService lineService;
    private final PathService pathService;

    public PathController(LineService lineService, PathService pathService) {
        this.lineService = lineService;
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@RequestParam @NotNull @NotBlank(message = "시작역이 없습니다!") String source,
                                                 @RequestParam @NotNull @NotBlank(message = "끝역이 없습니다!") String target,
                                                 @RequestParam @NotNull @NotBlank(message = "검색 기준을 선택해주세요") String type) {
        return ResponseEntity.ok()
                .body(pathService.calculatePath(source, target, type));
    }
}
