package com.pacal.share.entity.vo;

import com.pacal.share.entity.po.UserPO;
import com.pacal.share.enumm.UserRoleEnum;
import lombok.Data;

@Data
public class SessionUserVO {
    private Integer userId;
    private String username;
    private String avatar;
    private String roleName;
    private Integer roleId;
    private Integer gender;
    private String description;
    /** 鉴权 token */
    private String token;

    public static SessionUserVO from(UserPO po) {
        SessionUserVO vo = new SessionUserVO();
        vo.setUserId( po.getId() );
        vo.setUsername( po.getUsername() );
        vo.setAvatar( po.getAvatar() );
        vo.setDescription( po.getDescription() );
        vo.setGender( po.getGender() );
        vo.setRoleId( po.getRoleId() );
        vo.setRoleName( UserRoleEnum.getNameByRoleId( po.getRoleId() ) );
        return vo;
    }
}