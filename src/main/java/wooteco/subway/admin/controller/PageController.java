package wooteco.subway.admin.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import wooteco.subway.admin.repository.StationRepository;
import wooteco.subway.admin.service.LineService;

@Controller
public class PageController {
    private LineService lineService;
    private StationRepository stationRepository;

    public PageController(LineService lineService, StationRepository stationRepository) {
        this.lineService = lineService;
        this.stationRepository = stationRepository;
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String index() {
        return "admin/index";
    }

    @GetMapping(value = "/stations", produces = MediaType.TEXT_HTML_VALUE)
    public String stationPage(Model model) {
        model.addAttribute("stations", stationRepository.findAll());
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

    @GetMapping(value = "/lines/detail", produces = MediaType.TEXT_HTML_VALUE)
    public String servicePage(Model model) {
        return "service/index";
    }

    @GetMapping(value = "lines/map", produces = MediaType.TEXT_HTML_VALUE)
    public String mapPage(Model model) {
        return "service/map";
    }

    @GetMapping(value = "lines/search", produces = MediaType.TEXT_HTML_VALUE)
    public String searchPage(Model model) {
        return "service/search";
    }

    @GetMapping(value = "/lines/login", produces = MediaType.TEXT_HTML_VALUE)
    public String loginPage(Model model) {
        return "service/login";
    }

    @GetMapping(value = "/lines/join", produces = MediaType.TEXT_HTML_VALUE)
    public String joinPage(Model model) {
        return "service/join";
    }

    @GetMapping(value = "/lines/mypage", produces = MediaType.TEXT_HTML_VALUE)
    public String myPage(Model model) {
        return "service/mypage";
    }

    @GetMapping(value = "/lines/mypage-edit", produces = MediaType.TEXT_HTML_VALUE)
    public String myPageEditPage(Model model) {
        return "service/mypage-edit";
    }
}
