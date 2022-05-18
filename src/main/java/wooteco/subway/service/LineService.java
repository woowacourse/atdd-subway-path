package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.LineServiceRequest;
import wooteco.subway.service.dto.LineServiceResponse;

@Service
public class LineService {

    private final StationService stationService;
    private final LineDao lineDao;
    private final SectionService sectionService;

    public LineService(StationService stationService, LineDao lineDao, SectionService sectionService) {
        this.stationService = stationService;
        this.lineDao = lineDao;
        this.sectionService = sectionService;
    }

    @Transactional
    public LineServiceResponse save(LineServiceRequest lineServiceRequest) {
        Line savedLine = lineDao.save(new Line(lineServiceRequest.getName(), lineServiceRequest.getColor()));
        Station upStation = stationService.findById(lineServiceRequest.getUpStationId());
        Station downStation = stationService.findById(lineServiceRequest.getDownStationId());
        Section section = new Section(savedLine.getId(), upStation.getId(), downStation.getId(),
                lineServiceRequest.getDistance());
        sectionService.save(section);
        return new LineServiceResponse(savedLine, List.of(upStation, downStation));
    }

    @Transactional(readOnly = true)
    public List<LineServiceResponse> findAll() {
        List<Line> lines = lineDao.findAll();
        return lines.stream()
                .map(line -> new LineServiceResponse(line, stationService.findAllByLineId(line.getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineServiceResponse findById(Long id) {
        Line line = lineDao.findById(id);
        Sections sections = sectionService.findAllByLineId(id);
        List<Station> stations = sections.getSortedStationIdsInSingleLine().stream()
                .map(stationService::findById)
                .collect(Collectors.toList());

        return new LineServiceResponse(line, stations);
    }

    @Transactional
    public void update(Long id, String name, String color) {
        lineDao.updateById(id, new Line(name, color));
    }

    @Transactional
    public void deleteById(Long id) {
        lineDao.deleteById(id);
    }
}
