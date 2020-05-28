package wooteco.subway.admin.controller;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.domain.path.SubwayGraphStrategy;
import wooteco.subway.admin.dto.response.PathResponse;
import wooteco.subway.admin.dto.response.StandardResponse;
import wooteco.subway.admin.exception.DuplicatedValueException;
import wooteco.subway.admin.service.PathService;

@RestController
@RequestMapping("/paths")
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public StandardResponse<PathResponse> findPath(
        @RequestParam("source") @Valid @NotNull(message = "출발역을 입력해주세요.") Long sourceId,
        @RequestParam("target") @Valid @NotNull(message = "도착역을 입력해주세요.") Long targetId,
        @RequestParam("type") String type) {
        validate(sourceId, targetId);
        PathResponse response = pathService.findPath(new SubwayGraphStrategy<>(), sourceId,
            targetId, type);
        return StandardResponse.of(response);
    }

    private void validate(Long sourceId, Long targetId) {
        if (Objects.equals(sourceId, targetId)) {
            throw new DuplicatedValueException("출발역과 도착역이 같습니다.");
        }
    }
}
