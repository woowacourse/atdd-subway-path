package wooteco.subway.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.service.LineService;
import wooteco.subway.service.dto.LineResponse;
import wooteco.subway.ui.dto.LineRequest;

@RestController
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(
        @Validated @RequestBody LineRequest lineRequest) {
        LineResponse lineResponse = lineService.save(lineRequest.toServiceRequest());
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId()))
            .body(lineResponse);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> showLines() {
        List<LineResponse> lineResponse = lineService.findAll();
        return ResponseEntity.ok().body(lineResponse);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> findById(@PathVariable Long id) {
        LineResponse lineResponses = lineService.findById(id);
        return ResponseEntity.ok().body(lineResponses);
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id,
        @Valid @RequestBody LineRequest lineRequest) {
        lineService.updateById(id, lineRequest.toServiceRequest());
        return ResponseEntity.noContent().build();
    }
}
