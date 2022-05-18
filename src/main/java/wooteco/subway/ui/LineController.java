package wooteco.subway.ui;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.service.LineService;
import wooteco.subway.service.dto.LineServiceResponse;
import wooteco.subway.ui.dto.LineRequest;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineServiceResponse> createLine(
        @Validated @RequestBody LineRequest lineRequest) {
        LineServiceResponse lineServiceResponse = lineService.save(lineRequest.toServiceRequest());
        return ResponseEntity.created(URI.create("/lines/" + lineServiceResponse.getId()))
            .body(lineServiceResponse);
    }

    @GetMapping
    public ResponseEntity<List<LineServiceResponse>> showLines() {
        List<LineServiceResponse> lineServiceResponse = lineService.findAll();
        return ResponseEntity.ok().body(lineServiceResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineServiceResponse> findById(@PathVariable Long id) {
        LineServiceResponse lineServiceResponses = lineService.findById(id);
        return ResponseEntity.ok().body(lineServiceResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        if (lineService.deleteById(id)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id,
        @RequestBody LineRequest lineRequest) {
        if (lineService.updateById(id, lineRequest.toServiceRequest())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.noContent().build();
    }
}
