package wooteco.subway.admin.api;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import wooteco.subway.admin.path.controller.PathController;
import wooteco.subway.admin.path.service.PathService;
import wooteco.subway.admin.path.service.dto.PathInfoResponse;

@WebMvcTest(controllers = PathController.class)
public class PathControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PathService pathService;

    @Test
    void searchPath() throws Exception {
        String uri = "/paths";

        given(pathService.searchPath(anyLong(), anyLong())).willReturn(new PathInfoResponse());

        mockMvc.perform(get(uri)
            .param("source", "1")
            .param("target", "2"))
               .andDo(print())
               .andExpect(status().isOk());
    }

    @Test
    void searchPath_SourceIsNull() throws Exception {
        String uri = "/paths";

        given(pathService.searchPath(anyLong(), anyLong())).willReturn(new PathInfoResponse());

        mockMvc.perform(get(uri)
            .param("source", "null")
            .param("target", "2"))
               .andDo(print())
               .andExpect(status().isBadRequest());
    }

    @Test
    void searchPath_TargetIsNull() throws Exception {
        String uri = "/paths";

        given(pathService.searchPath(anyLong(), anyLong())).willReturn(new PathInfoResponse());

        mockMvc.perform(get(uri)
            .param("source", "1")
            .param("target", "null"))
               .andDo(print())
               .andExpect(status().isBadRequest());
    }

}
