package wooteco.subway.controller.dto;

import wooteco.subway.controller.dto.line.LineRequest;
import wooteco.subway.controller.dto.line.LineResponse;
import wooteco.subway.controller.dto.path.PathRequest;
import wooteco.subway.controller.dto.path.PathResponse;
import wooteco.subway.controller.dto.section.SectionRequest;
import wooteco.subway.controller.dto.station.StationResponse;
import wooteco.subway.service.dto.path.PathRequestDto;
import wooteco.subway.service.dto.path.PathResponseDto;
import wooteco.subway.service.dto.line.LineRequestDto;
import wooteco.subway.service.dto.line.LineResponseDto;
import wooteco.subway.service.dto.section.SectionRequestDto;
import wooteco.subway.service.dto.station.StationResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class ControllerDtoAssembler {

    private ControllerDtoAssembler() {
    }

    public static StationResponse stationResponseByDto(StationResponseDto stationResponseDto) {
        return new StationResponse(stationResponseDto.getId(), stationResponseDto.getName());
    }

    public static LineResponse lineResponseByDto(LineResponseDto lineResponseDto) {
        List<StationResponse> stations = lineResponseDto.getStations().stream()
                .map(it -> new StationResponse(it.getId(), it.getName()))
                .collect(Collectors.toList());
        return new LineResponse(lineResponseDto.getId(), lineResponseDto.getName(), lineResponseDto.getColor(),
                stations, lineResponseDto.getExtraFare());
    }

    public static LineRequestDto lineRequestDto(LineRequest lineRequest) {
        return new LineRequestDto(lineRequest.getName(), lineRequest.getColor(), lineRequest.getUpStationId(),
                lineRequest.getDownStationId(), lineRequest.getDistance(), lineRequest.getExtraFare());
    }

    public static SectionRequestDto sectionRequestDto(Long lineId, SectionRequest sectionRequest) {
        return new SectionRequestDto(lineId, sectionRequest.getUpStationId(), sectionRequest.getDownStationId(),
                sectionRequest.getDistance());
    }

    public static PathResponse pathResponse(PathResponseDto pathResponseDto) {
        List<StationResponse> stationResponses = pathResponseDto.getStations().stream()
                .map(it -> new StationResponse(it.getId(), it.getName()))
                .collect(Collectors.toList());
        return new PathResponse(stationResponses, pathResponseDto.getDistance(), pathResponseDto.getFare());
    }

    public static PathRequestDto pathRequestDto(PathRequest pathRequest) {
        return new PathRequestDto(pathRequest.getSource(), pathRequest.getTarget(), pathRequest.getAge());
    }
}
