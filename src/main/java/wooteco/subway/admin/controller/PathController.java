package wooteco.subway.admin.controller;

import java.util.Arrays;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.dto.MockPassInstance;
import wooteco.subway.admin.dto.StationResponse;

@RestController
@RequestMapping("/path")
public class PathController {

    @GetMapping("/source/{sourceId}/target/{targetId}")
    public ResponseEntity<MockPassInstance> findPath(@PathVariable String sourceId, @PathVariable String targetId) {
        return ResponseEntity.ok(MockPassInstance());
    }

    private MockPassInstance MockPassInstance() {
        return new MockPassInstance(
            Arrays.asList(new StationResponse(), new StationResponse(), new StationResponse(),
                new StationResponse(), new StationResponse(), new StationResponse()), 24L);
    }
}
