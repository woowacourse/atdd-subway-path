package wooteco.subway.admin.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ServicePageController {
    @GetMapping(value = "/service", produces = MediaType.TEXT_HTML_VALUE)
    public String serviceIndex() {
        return "service/index";
    }

    @GetMapping(value = "/service/map", produces = MediaType.TEXT_HTML_VALUE)
    public String serviceMap() {
        return "service/map";
    }

    @GetMapping(value = "/service/search", produces = MediaType.TEXT_HTML_VALUE)
    public String serviceSearch() {
        return "service/search";
    }
}
