package wooteco.subway.ui;

import java.beans.BeanProperty;
import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import wooteco.subway.dto.request.LineRequest;
import wooteco.subway.dto.response.LineResponse;
import wooteco.subway.service.LineService;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService service;

    public LineController(LineService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse response = service.save(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
    }

    @GetMapping
    public List<LineResponse> showLines() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public LineResponse showLine(@PathVariable Long id) {
        return service.findOne(id);
    }

    @PutMapping("/{id}")
    public void modifyLine(@PathVariable Long id, @RequestBody LineRequest lineRequest) {
        service.update(id, lineRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLine(@PathVariable Long id) {
        service.delete(id);
    }
}
