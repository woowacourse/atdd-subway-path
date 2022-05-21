package wooteco.subway.service;


import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.domain.Line;
import wooteco.subway.dto.request.LineRequest;
import wooteco.subway.dto.response.LineResponse;
import wooteco.subway.dto.request.SectionRequest;
import wooteco.subway.dto.response.StationResponse;
import wooteco.subway.exception.DuplicateLineException;

@Service
@Transactional
public class LineService {

    private final LineDao lineDao;
    private final StationService stationService;
    private final SectionService sectionService;

    public LineService(final LineDao lineDao,
                       final StationService stationService,
                       final SectionService sectionService) {
        this.lineDao = lineDao;
        this.stationService = stationService;
        this.sectionService = sectionService;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public LineResponse save(final LineRequest lineRequest) {
        final Line newLine = Line.createWithoutId(lineRequest.getName(), lineRequest.getColor(), lineRequest.getExtraFare());
        validateCreateRequest(newLine);
        final Long lineId = lineDao.save(newLine);
        final SectionRequest sectionRequest = extractSectionRequest(lineRequest);
        sectionService.firstSave(lineId, sectionRequest);
        return createLineResponse(lineDao.findById(lineId), getStationsByStationIds(lineId));
    }

    private void validateCreateRequest(final Line line) {
        validateName(line);
        validateColor(line);
    }

    private void validateName(final Line line) {
        if (lineDao.existByName(line)) {
            throw new DuplicateLineException("이미 존재하는 노선 이름입니다.");
        }
    }

    private void validateColor(final Line line) {
        if (lineDao.existByColor(line)) {
            throw new DuplicateLineException("이미 존재하는 노선 색깔입니다.");
        }
    }

    private SectionRequest extractSectionRequest(final LineRequest lineRequest) {
        return new SectionRequest(
                lineRequest.getUpStationId(),
                lineRequest.getDownStationId(),
                lineRequest.getDistance()
        );
    }

    private LineResponse createLineResponse(final Line newLine, final List<StationResponse> stations) {
        return new LineResponse(
                newLine.getId(), newLine.getName(), newLine.getColor(), newLine.getExtraFare(), stations
        );
    }

    private List<StationResponse> getStationsByStationIds(final Long line) {
        return stationService.findByStationIds(sectionService.findAllStationByLineId(line));
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<LineResponse> findAll() {
        return lineDao.findAll().stream()
                .map(line -> createLineResponse(line, getStationsByStationIds(line.getId())))
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public LineResponse findById(final Long lineId) {
        final Line line = lineDao.findById(lineId);
        return createLineResponse(line, getStationsByStationIds(line.getId()));
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void update(final Long lineId, final LineRequest lineRequest) {
        final Line newLine = Line.createWithoutId(lineRequest.getName(), lineRequest.getColor(), lineRequest.getExtraFare());
        validateUpdateRequest(lineId, newLine);
        lineDao.update(lineId, newLine);
    }

    private void validateUpdateRequest(final Long lineId, final Line line) {
        validateNameExceptSameId(lineId, line);
        validateColorExceptSameId(lineId, line);
    }

    private void validateNameExceptSameId(final Long lineId, final Line line) {
        if (lineDao.existByNameExceptSameId(lineId, line)) {
            throw new DuplicateLineException("이미 존재하는 노선 이름으로 업데이트할 수 없습니다.");
        }
    }

    private void validateColorExceptSameId(final Long lineId, final Line line) {
        if (lineDao.existByColorExceptSameId(lineId, line)) {
            throw new DuplicateLineException("이미 존재하는 노선 색깔로 업데이트할 수 없습니다.");
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void delete(final Long lineId) {
        lineDao.deleteById(lineId);
    }
}
