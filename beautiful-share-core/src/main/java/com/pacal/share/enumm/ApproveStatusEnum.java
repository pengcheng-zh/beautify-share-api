package com.pacal.share.enumm;

public enum ApproveStatusEnum {
    /** 待审核 */
    WAITING_APPROVE("A", "待审核"),
    /** 审核通过 */
    APPROVED("P", "审核通过"),
    /** 审核拒绝 */
    DISAPPROVED("R", "审核拒绝"),

    DELETED("C", "已删除");

    public final String status;
    public final String label;

    ApproveStatusEnum(String status, String label) {
        this.status = status;
        this.label = label;
    }

    public static String getLabelByStatus(String status) {
        for ( ApproveStatusEnum value : ApproveStatusEnum.values() ) {
            if ( value.status.equals( status ) ) {
                return value.label;
            }
        }
        return WAITING_APPROVE.label;
    }

    public static boolean isValid(String status) {
        if ( status == null ) return false;
        for ( ApproveStatusEnum value : ApproveStatusEnum.values() ) {
            if ( value.status.equals( status ) ) {
                return true;
            }
        }
        return false;
    }
}