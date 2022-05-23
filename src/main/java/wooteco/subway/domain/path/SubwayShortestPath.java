package wooteco.subway.domain.path;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.section.Section;
import wooteco.subway.domain.Station;

public class SubwayShortestPath {

	private final Map<Section, Line> values;

	public SubwayShortestPath(List<Line> lines) {
		values = new HashMap<>();
		for (Line line : lines) {
			line.getSections()
				.forEach(section -> values.put(section, line));
		}
	}

	public PathInfo find(Station source, Station target) {
		GraphPath<Station, SectionEdge> path = new DijkstraShortestPath<>(initGraph(values.keySet()))
			.getPath(source, target);
		return new PathInfo(path.getVertexList(), (int) path.getWeight(), getMaxExtraFare(path));
	}

	private Graph<Station, SectionEdge> initGraph(Set<Section> sections) {
		WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
		sections.forEach(section -> addSection(graph, section));
		return graph;
	}

	private void addSection(WeightedMultigraph<Station, SectionEdge> graph, Section section) {
		graph.addVertex(section.getUpStation());
		graph.addVertex(section.getDownStation());
		graph.addEdge(section.getUpStation(), section.getDownStation(), new SectionEdge(section));
	}

	private int getMaxExtraFare(GraphPath<Station, SectionEdge> path) {
		return path.getEdgeList().stream()
			.mapToInt(edge -> values.get(edge.getValue()).getExtraFare())
			.max()
			.orElseThrow();
	}

	private static class SectionEdge extends DefaultWeightedEdge {
		private final Section value;

		private SectionEdge(Section section) {
			this.value = section;
		}

		@Override
		protected double getWeight() {
			return value.getDistance();
		}

		private Section getValue() {
			return value;
		}
	}
}
