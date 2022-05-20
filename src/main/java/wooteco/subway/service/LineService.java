package wooteco.subway.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Sections;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.exception.LineDuplicationException;
import wooteco.subway.exception.NotExistLineException;

@Service
public class LineService {

    private static final int LINE_EXIST_VALUE = 1;
    private static final int DELETE_SUCCESS = 1;
    private static final int UPDATE_SUCCESS = 1;
    private static final String LINE_DUPLICATION = "이미 등록된 지하철 노선입니다.";
    private static final String LINE_NOT_EXIST = "존재하지 않은 지하철 노선입니다.";

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineService(final LineDao lineDao, final SectionDao sectionDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
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

        sectionDao.save(
                newLine.getId(),
                lineRequest.getUpStationId(),
                lineRequest.getDownStationId(),
                lineRequest.getDistance()
        );

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

    private List<StationResponse> createStationResponsesByLine(Line newLine) {
        Sections sections = new Sections(sectionDao.findAllByLineId(newLine.getId()));
        Set<Long> stationIds = sections.getStations();
        return stationIds.stream()
                .map(stationDao::getById)
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
    public LineResponse getById(final Long id) {
        Line line = lineDao.findById(id)
                .orElseThrow(() -> new NotExistLineException(LINE_NOT_EXIST));

        return new LineResponse(line.getId(), line.getName(), line.getColor(), createStationResponsesByLine(line),
                line.getExtraFare());
    }

    @Transactional
    public void update(final Long id, final LineRequest lineRequest) {
        int updateFlag = lineDao.update(id,
                new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getExtraFare()));

        if (updateFlag != UPDATE_SUCCESS) {
            throw new NotExistLineException(LINE_NOT_EXIST);
        }
    }

    @Transactional
    public void deleteById(final Long id) {
        if (lineDao.deleteById(id) != DELETE_SUCCESS) {
            throw new NotExistLineException(LINE_NOT_EXIST);
        }
    }
}
