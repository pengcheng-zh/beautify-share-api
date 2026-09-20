package com.pacal.share.controller;

import com.pacal.share.annotation.NeedLogin;
import com.pacal.share.common.BaseResponse;
import com.pacal.share.service.PostFavoriteService;
import com.pacal.share.utils.RequestUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post-favorite")
public class PostFavoriteController {
    @Resource
    PostFavoriteService favoriteService;
    /** 收藏 / 取消收藏 (重复调用即取反) */
    @NeedLogin
    @PostMapping("{postId}")
    public BaseResponse<String> toggleFavorite(@PathVariable("postId") Integer postId) {
        favoriteService.toggleFavorite(postId);
        return BaseResponse.SUCCESS( );
    }
}
