package wooteco.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.service.RouteService;

@RestController
public class PathController {

    private final RouteService routeService;

    public PathController(final RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> showLines(@RequestParam Long source, @RequestParam Long target,
                                                  @RequestParam int age) {
        return ResponseEntity.ok().body(routeService.createRoute(source, target, age));
    }
}
