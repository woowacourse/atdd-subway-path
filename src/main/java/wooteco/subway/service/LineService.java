package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.LineServiceRequest;
import wooteco.subway.service.dto.LineServiceResponse;

@Service
public class LineService {

    private final StationDao stationDao;
    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineService(StationDao stationDao, LineDao lineDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    @Transactional
    public LineServiceResponse save(LineServiceRequest lineServiceRequest) {
        Line savedLine = lineDao.save(new Line(lineServiceRequest.getName(), lineServiceRequest.getColor()));
        Station upStation = stationDao.findById(lineServiceRequest.getUpStationId());
        Station downStation = stationDao.findById(lineServiceRequest.getDownStationId());
        Section section = new Section(savedLine.getId(), upStation.getId(), downStation.getId(),
                lineServiceRequest.getDistance());
        sectionDao.save(section);
        return new LineServiceResponse(savedLine, List.of(upStation, downStation));
    }

    @Transactional(readOnly = true)
    public List<LineServiceResponse> findAll() {
        List<Line> lines = lineDao.findAll();
        return lines.stream()
                .map(line -> new LineServiceResponse(line, stationDao.findAllByLineId(line.getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineServiceResponse findById(Long id) {
        Line line = lineDao.findById(id);
        Sections sections = sectionDao.findAllByLineId(id);
        List<Station> stations = sections.getSortedStationIdsInSingleLine().stream()
                .map(stationDao::findById)
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
