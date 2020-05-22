package wooteco.subway.admin.dto;

import wooteco.subway.admin.domain.SearchType;

public class PathRequest {
    private String source;
    private String target;
    private SearchType type;

    private PathRequest() {
    }

    public PathRequest(String source, String target, SearchType type) {
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

    public SearchType getSearchType() {
        return type;
    }
}
