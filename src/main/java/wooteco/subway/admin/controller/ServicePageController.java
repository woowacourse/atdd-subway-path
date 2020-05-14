package wooteco.subway.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/service")
public class ServicePageController {

    @GetMapping
    public String index() {
        return "service/index";
    }

    @GetMapping("/search")
    public String search() {
        return "service/search";
    }
}
