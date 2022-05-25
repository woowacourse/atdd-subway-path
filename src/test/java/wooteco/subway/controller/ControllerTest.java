package wooteco.subway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import wooteco.subway.service.LineService;
import wooteco.subway.service.PathService;
import wooteco.subway.service.StationService;

@WebMvcTest({StationController.class, LineController.class, PathController.class})
public class ControllerTest {

	@Autowired
	protected MockMvc mockMvc;
	@Autowired
	protected ObjectMapper objectMapper;
	@MockBean
	protected StationService stationService;
	@MockBean
	protected LineService lineService;
	@MockBean
	protected PathService pathService;
}
