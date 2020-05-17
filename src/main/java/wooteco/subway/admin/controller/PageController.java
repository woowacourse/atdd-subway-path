package wooteco.subway.admin.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import wooteco.subway.admin.service.LineService;
import wooteco.subway.admin.service.StationService;

@Controller
public class PageController {
    private final LineService lineService;
    private final StationService stationService;

    public PageController(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String index() {
        return "service/index";
    }

    @GetMapping(value = "/map", produces = MediaType.TEXT_HTML_VALUE)
    public String map() {
        return "service/map";
    }

    @GetMapping(value = "/search", produces = MediaType.TEXT_HTML_VALUE)
    public String search() {
        return "service/search";
    }

    @GetMapping(value = "/admin", produces = MediaType.TEXT_HTML_VALUE)
    public String adminIndex() {
        return "admin/index";
    }

    @GetMapping(value = "/admin-station", produces = MediaType.TEXT_HTML_VALUE)
    public String stationPage(Model model) {
        model.addAttribute("stations", stationService.findAll());
        return "admin/admin-station";
    }

    @GetMapping(value = "/admin-line", produces = MediaType.TEXT_HTML_VALUE)
    public String linePage(Model model) {
        model.addAttribute("lines", lineService.showLines());
        return "admin/admin-line";
    }

    @GetMapping(value = "/admin-edge", produces = MediaType.TEXT_HTML_VALUE)
    public String edgePage(Model model) {
        return "admin/admin-edge";
    }
}
