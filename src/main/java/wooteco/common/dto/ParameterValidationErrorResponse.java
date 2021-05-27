package wooteco.common.dto;

import java.util.Map;

public class ParameterValidationErrorResponse extends ErrorResponse {

    private final Map<String, String> causes;

    public ParameterValidationErrorResponse(String message, Map<String, String> causes) {
        super(message);
        this.causes = causes;
    }

    public Map<String, String> getCauses() {
        return causes;
    }
}
