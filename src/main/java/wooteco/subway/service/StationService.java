package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.station.Station;
import wooteco.subway.exception.BadRequestException;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;
import wooteco.subway.service.dto.request.StationRequest;
import wooteco.subway.service.dto.response.StationResponse;

@Service
public class StationService {

    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public StationService(StationRepository stationRepository,
            SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public StationResponse create(StationRequest stationRequest) {
        Station station = new Station(stationRequest.getName());
        validateDuplicateName(station);
        Station savedStation = stationRepository.save(station);
        return new StationResponse(savedStation.getId(), savedStation.getName());
    }

    public List<StationResponse> showAll() {
        List<Station> stations = stationRepository.findAll();
        return stations.stream()
                .map(value -> new StationResponse(value.getId(), value.getName()))
                .collect(Collectors.toList());
    }

    public void removeById(Long id) {
        if (sectionRepository.existedByStation(id)) {
            throw new BadRequestException("삭제하려는 역이 구간에 존재합니다.");
        }
        stationRepository.deleteById(id);
    }

    private void validateDuplicateName(Station station) {
        if (stationRepository.existByName(station.getName())) {
            throw new BadRequestException("지하철 역 이름은 중복될 수 없습니다.");
        }
    }
}
