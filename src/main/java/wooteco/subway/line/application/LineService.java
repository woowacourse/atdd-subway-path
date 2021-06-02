package wooteco.subway.line.application;

import java.util.List;
import java.util.Objects;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.BusinessRelatedException;
import wooteco.subway.exception.ValidationFailureException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Lines;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
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
        try {
            Line persistLine = lineDao.insert(
                new Line(request.getName(), request.getColor())
            );
            persistLine.addSection(addInitSection(persistLine, request));
            routeMapManager.updateSections(findLines());
            return LineResponse.of(persistLine);
        } catch (DataAccessException e) {
            throw new BusinessRelatedException("노선 추가에 실패했습니다.");
        }
    }

    private Section addInitSection(Line line, LineRequest request) {
        validateSectionStations(request.getUpStationId(), request.getDownStationId());

        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
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
        try {
            return new Lines(lineDao.findAll());
        } catch (DataAccessException e) {
            throw new BusinessRelatedException("노선 조회에 실패했습니다.");
        }
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    private Line findLineById(Long id) {
        try {
            return lineDao.findById(id);
        } catch (DataAccessException e) {
            throw new BusinessRelatedException("노선 조회에 실패했습니다.");
        }
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        try {
            lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
            routeMapManager.updateSections(findLines());
        } catch (DataAccessException e) {
            throw new BusinessRelatedException(
                String.format("노선 정보 수정에 실패했습니다. (%s)", lineUpdateRequest.getName())
            );
        }
    }

    @Transactional
    public void deleteLineById(Long id) {
        try {
            lineDao.deleteById(id);
            routeMapManager.updateSections(findLines());
        } catch (DataAccessException e) {
            throw new BusinessRelatedException("노선 제거에 실패했습니다.");
        }
    }

    @Transactional
    public void addLineStation(Long lineId, SectionRequest request) {
        validateSectionStations(request.getUpStationId(), request.getDownStationId());

        try {
            Line line = findLineById(lineId);
            Station upStation = stationService.findStationById(request.getUpStationId());
            Station downStation = stationService.findStationById(request.getDownStationId());
            line.addSection(upStation, downStation, request.getDistance());

            sectionDao.deleteByLineId(lineId);
            sectionDao.insertSections(line);
            routeMapManager.updateSections(findLines());
        } catch (DataAccessException e) {
            throw new BusinessRelatedException("구간 추가에 실패했습니다.");
        }
    }

    @Transactional
    public void removeLineStation(Long lineId, Long stationId) {
        try {
            Line line = findLineById(lineId);
            Station station = stationService.findStationById(stationId);
            line.removeSection(station);

            sectionDao.deleteByLineId(lineId);
            sectionDao.insertSections(line);
            routeMapManager.updateSections(findLines());
        } catch (DataAccessException e) {
            throw new BusinessRelatedException("구간 제거에 실패했습니다.");
        }
    }
}
