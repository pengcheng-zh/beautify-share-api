package com.pacal.share.entity.vo;

import lombok.Data;

@Data
public class MineSummaryVO {
    private Integer postCount;
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer shareCount;

    public MineSummaryVO(int postCount, int likeCount, int favoriteCount) {
        this.postCount = postCount;
        this.likeCount = likeCount;
        this.favoriteCount = favoriteCount;
    }
}
