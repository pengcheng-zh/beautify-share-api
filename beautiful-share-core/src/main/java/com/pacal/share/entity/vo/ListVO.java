package com.pacal.share.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class ListVO<T> {
    private Integer total;
    private Integer page;
    private Integer pageSize;
    private List<T> data;

    public static <T> ListVO<T> of(int total, int pageIndex, int pageSize, List<T> data) {
        ListVO<T> vo = new ListVO<>();
        vo.setTotal( total );
        vo.setPage( pageIndex );
        vo.setPageSize( pageSize );
        vo.setData( data );
        return vo;
    }
}