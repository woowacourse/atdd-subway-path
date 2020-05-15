package wooteco.subway.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
public class AdminPageController {

	@GetMapping
	public String index() {
		return "/admin/index";
	}

	@GetMapping("/station")
	public String adminStation() {
		return "/admin/admin-station";
	}

	@GetMapping("/edge")
	public String adminEdge() {
		return "/admin/admin-edge";
	}

	@GetMapping("/line")
	public String adminLine() {
		return "/admin/admin-line";
	}
}
