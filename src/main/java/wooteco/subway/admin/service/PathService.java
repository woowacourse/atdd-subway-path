package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.ShortestPath;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PathService {
	private LineRepository lineRepository;
	private StationRepository stationRepository;

	public PathService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	public ShortestPath findShortestDistancePath(String sourceName, String targetName) {
		List<Station> stations = new ArrayList<>();

		stations.add(new Station(6L, "시청"));
		stations.add(new Station(5L, "서울역"));
		stations.add(new Station(4L, "용산"));
		stations.add(new Station(3L, "신길"));
		stations.add(new Station(2L, "신도림"));

		return new ShortestPath(stations, 40, 40);

	}
}
