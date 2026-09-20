package com.pacal.share.context;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;

@Data
public class PacalContext {

    /** jwt */
    private DecodedJWT decodedJWT;

    /** user id */
    private String userId;

    /** 角色id (来自 jwt role_id claim) */
    private String roleName;

    /** ip地址 */
    private String ipAddress;
}