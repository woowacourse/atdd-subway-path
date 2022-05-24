package wooteco.subway.ui.dto;

import static wooteco.subway.ui.dto.LineCreationRequest.OMISSION_MESSAGE;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import wooteco.subway.service.dto.LineModificationServiceRequest;

public class LineModificationRequest {

    @NotBlank(message = "노선의 이름" + OMISSION_MESSAGE)
    private String name;
    @NotBlank(message = "노선의 색상" + OMISSION_MESSAGE)
    private String color;
    @NotNull(message = "추가 요금 값" + OMISSION_MESSAGE)
    @Min(value = 1, message = "추가 요금 값" + OMISSION_MESSAGE)
    private Integer extraFare;

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Integer getExtraFare() {
        return extraFare;
    }

    public LineModificationServiceRequest toServiceRequest() {
        return new LineModificationServiceRequest(name, color, extraFare);
    }
}
