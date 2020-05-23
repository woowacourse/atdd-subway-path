package wooteco.subway.admin.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import wooteco.subway.admin.service.LineService;
import wooteco.subway.admin.service.StationService;

@Controller
public class PageController {
    private LineService lineService;
    private StationService stationService;

    public PageController(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String index() {
        return "admin/index";
    }

    @GetMapping(value = "/stations", produces = MediaType.TEXT_HTML_VALUE)
    public String stationPage(Model model) {
        model.addAttribute("stations", stationService.show());
        return "admin/admin-station";
    }

    @GetMapping(value = "/lines", produces = MediaType.TEXT_HTML_VALUE)
    public String linePage(Model model) {
        model.addAttribute("lines", lineService.showLines());
        return "admin/admin-line";
    }

    @GetMapping(value = "/edges", produces = MediaType.TEXT_HTML_VALUE)
    public String edgePage(Model model) {
        return "admin/admin-edge";
    }

    @GetMapping(value = "/search", produces = MediaType.TEXT_HTML_VALUE)
    public String searchPage(Model model) {
        return "service/search";
    }
}
