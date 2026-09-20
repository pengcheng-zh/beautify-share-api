package com.pacal.share.entity.vo;

import com.pacal.share.entity.po.UserPO;
import lombok.Data;

@Data
public class UserSimpleVO {
    private Integer id;
    private String username;
    private String avatar;

    public static UserSimpleVO from(UserPO po) {
        UserSimpleVO vo = new UserSimpleVO();
        vo.setId(po.getId());
        vo.setUsername(po.getUsername());
        vo.setAvatar(po.getAvatar());
        return vo;
    }
}
