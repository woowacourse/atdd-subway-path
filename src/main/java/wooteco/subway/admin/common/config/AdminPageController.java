package wooteco.subway.admin.common.config;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
public class AdminPageController {

	@GetMapping(produces = MediaType.TEXT_HTML_VALUE)
	public String index() {
		return "admin/index";
	}

	@GetMapping(value = "/station", produces = MediaType.TEXT_HTML_VALUE)
	public String stationPage() {
		return "admin/station";
	}

	@GetMapping(value = "/line", produces = MediaType.TEXT_HTML_VALUE)
	public String linePage() {
		return "admin/line";
	}

	@GetMapping(value = "/edge", produces = MediaType.TEXT_HTML_VALUE)
	public String edgePage() {
		return "admin/edge";
	}

}
