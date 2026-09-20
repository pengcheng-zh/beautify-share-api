package com.pacal.share.common;

public final class RedisKey {
    /** 防重复提交: ua:{userId}:{action}:{argsHash} */
    public static final String USER_ACTION = "ua:%s:%s";

    /** 热门文章 */
    public static final String HOT_POST = "hotpost";

    private RedisKey() {}
}