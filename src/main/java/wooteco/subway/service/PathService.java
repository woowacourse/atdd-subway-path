package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.repository.LineRepository;
import wooteco.subway.domain.Lines;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Station;

@Service
@Transactional(readOnly = true)
public class PathService {

	private final LineRepository lineRepository;

	public PathService(LineRepository lineRepository) {
		this.lineRepository = lineRepository;
	}

	public Path findPath(Station source, Station target) {
		Lines lines = new Lines(lineRepository.findAll());
		return lines.findPath(source, target);
	}
}
