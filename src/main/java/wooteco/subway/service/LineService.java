package wooteco.subway.service;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.List;
import java.util.Optional;
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
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.exception.NotExistException;

@Service
@Transactional
public class LineService {

    private static final int DELETE_FAIL = 0;

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineService(LineDao lineDao, SectionDao sectionDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public LineResponse save(LineRequest request) {
        Line line = new Line(request.getName(), request.getColor(), request.getExtraFare());
        final Line savedLine = lineDao.save(line);

        final Section section = new Section(savedLine.getId(), request.getUpStationId(), request.getDownStationId(),
                request.getDistance());
        sectionDao.save(section);

        return new LineResponse(savedLine, makeStationResponseList(request));
    }

    private List<StationResponse> makeStationResponseList(LineRequest request) {
        final Station upStation = getStationById(request.getUpStationId());
        final Station downStation = getStationById(request.getDownStationId());

        final StationResponse upStationResponse = new StationResponse(upStation);
        final StationResponse downStationResponse = new StationResponse(downStation);

        return List.of(upStationResponse, downStationResponse);
    }

    private Station getStationById(Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new NotExistException("찾으려는 역이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public LineResponse findById(Long id) {
        final Line line = lineDao.findById(id)
                .orElseThrow(() -> new NotExistException("찾으려는 노선이 존재하지 않습니다."));

        final Sections sections = new Sections(sectionDao.findByLineId(line.getId()));

        return new LineResponse(line, getStationResponses(sections));
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        return lineDao.findAll()
                .stream()
                .map(line -> new LineResponse(line, getStationResponses(getSections(line))))
                .collect(toUnmodifiableList());
    }

    private Sections getSections(Line line) {
        final List<Section> sections = sectionDao.findByLineId(line.getId());

        return new Sections(sections);
    }

    private List<StationResponse> getStationResponses(Sections sections) {
        final List<Long> stationIds = sections.getStationIds();
        return stationIds.stream()
                .map(stationDao::findById)
                .map(Optional::orElseThrow)
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }

    public Long updateByLine(Long id, LineRequest request) {
        final Line updateLine = new Line(id, request.getName(), request.getColor(), request.getExtraFare());
        return lineDao.updateByLine(updateLine);
    }

    public void deleteById(Long id) {
        final int isDeleted = lineDao.deleteById(id);

        if (isDeleted == DELETE_FAIL) {
            throw new NotExistException("존재하지 않는 노선입니다.");
        }
        sectionDao.deleteByLineId(id);
    }
}
