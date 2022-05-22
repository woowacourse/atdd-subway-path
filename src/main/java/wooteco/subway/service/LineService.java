package wooteco.subway.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.dto.line.LineRequest;
import wooteco.subway.dto.line.LineResponse;
import wooteco.subway.dto.section.SectionRequest;

@Service
public class LineService {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineService(LineDao lineDao, SectionDao sectionDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public LineResponse find(Long lineId) {
        var sections = sectionDao.findByLineId(lineId);

        var stations = sections.stream()
                .map(it -> stationDao.findBySection(new SectionRequest(it)))
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

        var line = lineDao.find(lineId);

        return new LineResponse(line, stations);
    }

    public List<LineResponse> findAll() {
        return lineDao.findAll().stream()
                .map(LineResponse::getId)
                .map(this::find)
                .collect(Collectors.toList());
    }

    @Transactional
    public LineResponse create(LineRequest lineRequest) {
        var upStationId = lineRequest.getUpStationId();
        var downStationId = lineRequest.getDownStationId();

        var line = lineDao.create(lineRequest);

        sectionDao.create(line.getId(), new SectionRequest(lineRequest));

        var stations = Stream.of(stationDao.findById(upStationId), stationDao.findById(downStationId))
                .collect(Collectors.toList());

        return new LineResponse(line, stations);
    }

    public void update(Long id, LineRequest lineRequest) {
        lineDao.update(id, lineRequest);
    }

    public void deleteById(Long id) {
        lineDao.deleteById(id);
    }
}
