package wooteco.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paths")
public class PathController {

    @GetMapping
    public ResponseEntity<Void> showPath(@RequestParam(value = "source") final Long sourceStationId,
                                     @RequestParam(value = "target") final Long targetStationId) {
        return ResponseEntity.badRequest().build();
    }
}
