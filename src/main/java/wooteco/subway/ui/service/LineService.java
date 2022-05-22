package wooteco.subway.ui.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.LineRequest;
import wooteco.subway.dto.response.LineResponse;

@Service
public class LineService {
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public LineService(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Transactional
    public LineResponse create(LineRequest lineRequest) {
        String name = lineRequest.getName();
        String color = lineRequest.getColor();

        Station upStation = findStationById(lineRequest.getUpStationId());
        Station downStation = findStationById(lineRequest.getDownStationId());
        Section section = new Section(upStation, downStation, lineRequest.getDistance());

        Line line = new Line(name, color, section);
        Line createdLine = saveLine(line);
        sectionDao.save(section, createdLine.getId());

        return LineResponse.from(createdLine);
    }

    private Station findStationById(Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "에 해당하는 역을 찾을 수 없습니다."));
    }

    private Line saveLine(Line line) {
        try {
            return lineDao.save(line);
        } catch (DuplicateKeyException e) {
            throw new IllegalStateException(line.getName() + "은(는) 이미 존재하는 노선 이름입니다.");
        }
    }

    public List<LineResponse> findAll() {
        final List<Line> lines = lineDao.findAll();
        return lines.stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public LineResponse findById(Long id) {
        final Line line = lineDao.findById(id);
        return LineResponse.from(line);
    }

    public void modify(Long id, LineRequest lineRequest) {
        final Line line = new Line(id, lineRequest.getName(), lineRequest.getColor());
        lineDao.update(line);
    }

    public void delete(Long id) {
        lineDao.delete(id);
    }
}
