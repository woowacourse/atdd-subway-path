package wooteco.subway.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.SectionRepository;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.exception.LineDuplicationException;
import wooteco.subway.exception.LineNotFoundException;

@Service
public class LineService {

    private static final int LINE_EXIST_VALUE = 1;
    private static final int DELETE_SUCCESS = 1;
    private static final int UPDATE_SUCCESS = 1;
    private static final String LINE_DUPLICATION = "이미 등록된 지하철 노선입니다.";
    private static final String LINE_NOT_EXIST = "존재하지 않은 지하철 노선입니다.";

    private final LineDao lineDao;
    private SectionRepository sectionRepository;
    private final StationDao stationDao;

    public LineService(final LineDao lineDao, final SectionRepository sectionRepository, StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionRepository = sectionRepository;
        this.stationDao = stationDao;
    }

    @Transactional
    public LineResponse save(final LineRequest lineRequest) {
        final Line line = new Line(
                lineRequest.getName(),
                lineRequest.getColor(),
                lineRequest.getExtraFare()
        );
        validateDuplication(line);
        final Line newLine = lineDao.save(line);

        sectionRepository.saveSection(new Section(newLine, lineRequest.getUpStationId(), lineRequest.getDownStationId(),
                lineRequest.getDistance()));

        List<StationResponse> stationResponses = createStationResponsesByLine(newLine);

        return new LineResponse(
                newLine.getId(),
                newLine.getName(),
                newLine.getColor(),
                stationResponses,
                lineRequest.getExtraFare()
        );
    }

    private void validateDuplication(Line line) {
        int existFlag = lineDao.isExistLine(line);
        if (existFlag == LINE_EXIST_VALUE) {
            throw new LineDuplicationException(LINE_DUPLICATION);
        }
    }

    public List<StationResponse> createStationResponsesByLine(Line newLine) {
        List<Section> values = sectionRepository.getSectionsByLineId(newLine.getId());
        Sections sections = new Sections(values);
        Set<Long> stationIds = sections.getStations();
        List<Station> stations = stationDao.getByIds(new ArrayList<>(stationIds));

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        final List<Line> lines = lineDao.findAll();
        return lines.stream()
                .map(it -> new LineResponse(it.getId(), it.getName(), it.getColor(), createStationResponsesByLine(it),
                        it.getExtraFare()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Line getById(final Long id) {
        return lineDao.findById(id)
                .orElseThrow(() -> new LineNotFoundException(LINE_NOT_EXIST));
    }

    @Transactional
    public void update(final Long id, final LineRequest lineRequest) {
        int updateFlag = lineDao.update(id,
                new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getExtraFare()));

        if (updateFlag != UPDATE_SUCCESS) {
            throw new LineNotFoundException(LINE_NOT_EXIST);
        }
    }

    @Transactional
    public void deleteById(final Long id) {
        if (lineDao.deleteById(id) != DELETE_SUCCESS) {
            throw new LineNotFoundException(LINE_NOT_EXIST);
        }
    }
}
