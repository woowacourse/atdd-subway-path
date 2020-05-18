package wooteco.subway.admin.dto.request;

public class PathSearchRequest {
	private final String source;
	private final String target;
	private final String type;

	public PathSearchRequest(String source, String target, String type) {
		this.source = source;
		this.target = target;
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public String getTarget() {
		return target;
	}

	public String getType() {
		return type;
	}
}