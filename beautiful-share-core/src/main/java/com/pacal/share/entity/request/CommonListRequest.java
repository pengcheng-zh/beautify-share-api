package com.pacal.share.entity.request;

import lombok.Data;

@Data
public class CommonListRequest {
    /** 页码,从 1 开始 */
    private Integer page = 1;
    /** 页大小 */
    private Integer pageSize = 20;
    /** 关键字 */
    private String keyword;
    private String status;

    public int getPageIndex() {
        return page == null || page < 1 ? 1 : page;
    }

    public int getPageSize() {
        if ( pageSize == null || pageSize <= 0 ) return 20;
        return Math.min( pageSize, 100 );
    }

    public int getOffset() {
        return (getPageIndex() - 1) * getPageSize();
    }
}