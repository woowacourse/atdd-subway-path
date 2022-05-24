package wooteco.subway.ui.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.dto.request.LineCreateRequest;
import wooteco.subway.dto.response.LineCreateResponse;
import wooteco.subway.ui.service.LineService;
import wooteco.subway.ui.service.SectionService;

@RestController
public class LineController {
    private final LineService lineService;
    private final SectionService sectionService;

    public LineController(LineService lineService, SectionService sectionService) {
        this.lineService = lineService;
        this.sectionService = sectionService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineCreateResponse> createLine(@RequestBody LineCreateRequest lineCreateRequest) {
        LineCreateResponse lineCreateResponse = lineService.create(lineCreateRequest);
        Long id = lineCreateResponse.getId();
        return ResponseEntity.created(URI.create("/lines/" + id)).body(lineCreateResponse);
    }

    @GetMapping(value = "/lines")
    public ResponseEntity<List<LineCreateResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.findAll());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineCreateResponse> showLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.findById(id));
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<Void> modifyLine(@PathVariable Long id, @RequestBody LineCreateRequest lineCreateRequest) {
        lineService.modify(id, lineCreateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        sectionService.deleteByLine(id);
        lineService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
