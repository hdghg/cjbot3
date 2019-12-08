package com.gitlab.hdghg.cjbot3.model.bing;

import java.util.List;

public class WebPages {

    private List<Value> value;
    private Long totalEstimatedMatches;

    public List<Value> getValue() {
        return value;
    }

    public void setValue(List<Value> value) {
        this.value = value;
    }

    public Long getTotalEstimatedMatches() {
        return totalEstimatedMatches;
    }

    public void setTotalEstimatedMatches(Long totalEstimatedMatches) {
        this.totalEstimatedMatches = totalEstimatedMatches;
    }
}
