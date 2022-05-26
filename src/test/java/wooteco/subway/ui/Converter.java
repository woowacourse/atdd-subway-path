package wooteco.subway.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import org.springframework.test.web.servlet.MvcResult;
import wooteco.subway.dto.response.ErrorResponse;

public class Converter {

    public static String ConvertToStringFrom(final MvcResult result, final ObjectMapper objectMapper)
            throws UnsupportedEncodingException, JsonProcessingException {
        return objectMapper.readValue(result.getResponse()
                .getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class)
                .getMessage();
    }
}
