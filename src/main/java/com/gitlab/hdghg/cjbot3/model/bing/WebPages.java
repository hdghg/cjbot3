package com.gitlab.hdghg.cjbot3.model.bing;

import java.util.List;

public class WebPages {

    private List<WebPage> value;
    private Long totalEstimatedMatches;

    public List<WebPage> getValue() {
        return value;
    }

    public void setValue(List<WebPage> value) {
        this.value = value;
    }

    public Long getTotalEstimatedMatches() {
        return totalEstimatedMatches;
    }

    public void setTotalEstimatedMatches(Long totalEstimatedMatches) {
        this.totalEstimatedMatches = totalEstimatedMatches;
    }
}
