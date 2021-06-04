package wooteco.subway.line.application;

import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.application.ValidationFailureException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Lines;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.LineUpdateRequest;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.routemap.application.RouteMapManager;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

@Service
public class LineService {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationService stationService;
    private final RouteMapManager routeMapManager;

    public LineService(LineDao lineDao, SectionDao sectionDao, StationService stationService,
        RouteMapManager routeMapManager) {

        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationService = stationService;
        this.routeMapManager = routeMapManager;
    }

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineDao.insert(
            new Line(request.getName(), request.getColor())
        );
        persistLine.addSection(addInitSection(persistLine, request));
        routeMapManager.updateSections(findLines());
        return LineResponse.of(persistLine);
    }

    private Section addInitSection(Line line, LineRequest request) {
        validateSectionStations(request.getUpStationId(), request.getDownStationId());

        Station upStation = stationService.findExistentStationById(request.getUpStationId());
        Station downStation = stationService.findExistentStationById(request.getDownStationId());
        Section section = new Section(upStation, downStation, request.getDistance());
        return sectionDao.insert(line, section);
    }

    private void validateSectionStations(Long upStationId, Long downStationId) {
        validateStationsNonNull(upStationId, downStationId);
        validateDifferentStations(upStationId, downStationId);
    }

    private void validateStationsNonNull(Long upStationId, Long downStationId) {
        if (Objects.isNull(upStationId) || Objects.isNull(downStationId)) {
            throw new ValidationFailureException("추가하려는 구간의 상행역과 하행역에 null이 있습니다.");
        }
    }

    private void validateDifferentStations(Long upStationId, Long downStationId) {
        if (upStationId.equals(downStationId)) {
            throw new ValidationFailureException("추가하려는 구간의 상행역과 하행역이 같습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findLineResponses() {
        return LineResponse.listOf(findLines());
    }

    private Lines findLines() {
        return new Lines(lineDao.findAll());
    }

    public LineResponse findLineResponseById(Long id) {
        return LineResponse.of(findLineById(id));
    }

    private Line findLineById(Long id) {
        return lineDao.findById(id);
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
        routeMapManager.updateSections(findLines());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
        routeMapManager.updateSections(findLines());
    }

    @Transactional
    public void addLineStation(Long lineId, SectionRequest request) {
        validateSectionStations(request.getUpStationId(), request.getDownStationId());

        Line line = findLineById(lineId);
        Station upStation = stationService.findExistentStationById(request.getUpStationId());
        Station downStation = stationService.findExistentStationById(request.getDownStationId());
        line.addSection(upStation, downStation, request.getDistance());

        sectionDao.deleteByLineId(lineId);
        sectionDao.insertSections(line);
        routeMapManager.updateSections(findLines());
    }

    @Transactional
    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findExistentStationById(stationId);
        line.removeSection(station);

        sectionDao.deleteByLineId(lineId);
        sectionDao.insertSections(line);
        routeMapManager.updateSections(findLines());
    }
}
