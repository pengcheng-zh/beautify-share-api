package com.pacal.share.enumm;

public enum UserRoleEnum {
    /** 超级管理员 */
    ADMIN(1, "超级管理员"),
    /** 普通管理员 */
    NORMAL_ADMIN(2, "管理员"),
    /** 普通用户 */
    NORMAL(3, "普通用户");

    public final Integer roleId;
    public final String name;

    UserRoleEnum(Integer roleId, String name) {
        this.roleId = roleId;
        this.name = name;
    }

    public static String getNameByRoleId(int roleId) {
        for ( UserRoleEnum value : UserRoleEnum.values() ) {
            if ( value.roleId == roleId ) {
                return value.name;
            }
        }
        return NORMAL.name;
    }
}