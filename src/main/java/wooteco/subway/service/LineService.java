package wooteco.subway.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;
import wooteco.subway.service.dto.LineResponse;
import wooteco.subway.service.dto.LineServiceRequest;
import wooteco.subway.service.dto.StationResponse;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository,
        SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public LineResponse save(LineServiceRequest lineServiceRequest) {
        validateDuplicationName(lineServiceRequest.getName());
        Line line = new Line(lineServiceRequest.getName(), lineServiceRequest.getColor(), lineServiceRequest.getExtraFare());
        Long savedId = lineRepository.save(line);
        Line insertLine = new Line(savedId, line.getName(), line.getColor(), line.getExtraFare());
        sectionRepository.save(new Section(insertLine, lineServiceRequest.getUpStationId(),
            lineServiceRequest.getDownStationId(), lineServiceRequest.getDistance()));

        return new LineResponse(savedId, line.getName(), line.getColor(), List.of(
            findStationByLineId(lineServiceRequest.getUpStationId()),
            findStationByLineId(lineServiceRequest.getDownStationId())
        ));
    }

    private void validateDuplicationName(String name) {
        if (lineRepository.existsByName(name)) {
            throw new IllegalArgumentException("중복된 이름이 존재합니다.");
        }
    }

    public List<LineResponse> findAll() {
        Map<Long, Station> stations = findAllStations();
        return lineRepository.findAll().stream()
            .map(i -> new LineResponse(i.getId(), i.getName(), i.getColor(),
                getSortedStationsByLineId(i.getId(), stations)))
            .collect(Collectors.toList());
    }

    private StationResponse findStationByLineId(Long lineId) {
        Station station = stationRepository.findById(lineId);
        return new StationResponse(station.getId(), station.getName());
    }

    private Map<Long, Station> findAllStations() {
        return stationRepository.findAll().stream()
            .collect(Collectors.toMap(Station::getId, i -> new Station(i.getId(), i.getName())));
    }

    private List<StationResponse> getSortedStationsByLineId(Long lineId,
        Map<Long, Station> stations) {
        Sections sections = new Sections(sectionRepository.findByLineId(lineId));
        List<Long> stationIds = sections.sortedStationId();

        return stationIds.stream()
            .map(i -> toStationResponse(stations.get(i)))
            .collect(Collectors.toList());
    }

    private StationResponse toStationResponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public boolean deleteById(Long id) {
        return lineRepository.deleteById(id);
    }

    public boolean updateById(Long id, LineServiceRequest lineServiceRequest) {
        Line line = new Line(id, lineServiceRequest.getName(), lineServiceRequest.getColor(), lineServiceRequest.getExtraFare());
        return lineRepository.updateById(line);
    }

    public LineResponse findById(Long id) {
        Line line = lineRepository.findById(id);
        List<Station> stations = findSortedStationByLineId(line.getId());
        return new LineResponse(line.getId(), line.getName(), line.getColor(),
            toStationResponse(stations));
    }

    private List<StationResponse> toStationResponse(List<Station> stations) {
        return stations.stream()
            .map(i -> new StationResponse(i.getId(), i.getName()))
            .collect(Collectors.toList());
    }

    private List<Station> findSortedStationByLineId(Long lineId) {
        Sections sections = new Sections(sectionRepository.findByLineId(lineId));
        List<Long> stationIds = sections.sortedStationId();
        return stationIds.stream()
            .map(stationRepository::findById)
            .collect(Collectors.toList());
    }
}
