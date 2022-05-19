package wooteco.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.service.RouteService;
import wooteco.subway.ui.dto.response.RouteResponse;

@RestController
@RequestMapping("/paths")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<RouteResponse> findRoute(@RequestParam Long source, @RequestParam Long target,
                                                   @RequestParam int age) {
        final RouteResponse routeResponse = routeService.findRoute(source, target, age);
        return ResponseEntity.ok().body(routeResponse);
    }
}
