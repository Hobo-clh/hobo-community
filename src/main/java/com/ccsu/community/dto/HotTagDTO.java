package com.ccsu.community.dto;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class HotTagDTO implements Comparable{

    private String name;
    private Integer priority;

    @Override
    public int compareTo(@NotNull Object o) {
        return this.getPriority() - ((HotTagDTO)o).getPriority();
    }
}
