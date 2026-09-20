package com.pacal.share.entity.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private Integer gender;
    private String username;
    private String avatar;
    private String description;
}
