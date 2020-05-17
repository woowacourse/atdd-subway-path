package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.dto.SearchPathResponse;
import wooteco.subway.admin.service.PathService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<SearchPathResponse> searchPath(HttpServletRequest request) {
        String startStationName = request.getParameter("startStationName");
        String targetStationName = request.getParameter("targetStationName");
        String type = request.getParameter("type");

        return ResponseEntity.ok().body(pathService.searchPath(startStationName, targetStationName, type));
    }
}
