package wooteco.subway.admin.common.config;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import wooteco.subway.admin.line.service.LineService;
import wooteco.subway.admin.station.repository.StationRepository;

@Controller
public class ServicePageController {

	private final LineService lineService;
	private final StationRepository stationRepository;

	public ServicePageController(LineService lineService, StationRepository stationRepository) {
		this.lineService = lineService;
		this.stationRepository = stationRepository;
	}

	@GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
	public String index() {
		return "service/index";
	}

	@GetMapping(value = "/login", produces = MediaType.TEXT_HTML_VALUE)
	public String login() {
		return "service/login";
	}

	@GetMapping(value = "/join", produces = MediaType.TEXT_HTML_VALUE)
	public String join() {
		return "service/join";
	}

	@GetMapping(value = "/mypage", produces = MediaType.TEXT_HTML_VALUE)
	public String mypage() {
		return "service/mypage";
	}

	@GetMapping(value = "/mypage-edit", produces = MediaType.TEXT_HTML_VALUE)
	public String myPageEdit() {
		return "service/mypage-edit";
	}

	@GetMapping(value = "/map", produces = MediaType.TEXT_HTML_VALUE)
	public String map() {
		return "service/map";
	}

	@GetMapping(value = "/search", produces = MediaType.TEXT_HTML_VALUE)
	public String search() {
		return "service/search";
	}
}
